package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.Config.VNPAYConfig;
import com.example.Cap2.NannyNow.DTO.Response.CareTakerFeedBackRes;
import com.example.Cap2.NannyNow.DTO.Response.PaymentDTO;
import com.example.Cap2.NannyNow.DTO.Response.PaymentRes;
import com.example.Cap2.NannyNow.Entity.Booking;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import com.example.Cap2.NannyNow.Entity.Payment;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.PaymentMapper;
import com.example.Cap2.NannyNow.Repository.BookingRepository;
import com.example.Cap2.NannyNow.Repository.CareTakerRepository;
import com.example.Cap2.NannyNow.Repository.PaymentRepository;
import com.example.Cap2.NannyNow.Util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    PaymentMapper paymentMapper;
    PaymentRepository paymentRepository;
    CareTakerRepository careTakerRepository;
    BookingRepository bookingRepository;
    VNPAYConfig config;

    public String createPaymentUrl(HttpServletRequest req, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOKING_NOT_FOUND));

        try {
            String vnp_TxnRef = String.valueOf(System.currentTimeMillis()); // Random Transaction
            String vnp_OrderInfo = String.valueOf(booking.getBookingId());  // Save bookingId
            String orderType = "other";

            float amount = booking.getPayment().getPrice();
            String vnp_Amount = String.valueOf((long) (amount * 100)); // nhân 100

            String vnp_Locale = "vn";
            String vnp_BankCode = "NCB";
            String vnp_IpAddr = req.getRemoteAddr();

            Calendar cld = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", config.getApiVersion());
            vnp_Params.put("vnp_Command", config.getCommand());
            vnp_Params.put("vnp_TmnCode", config.getTmnCode());
            vnp_Params.put("vnp_Amount", vnp_Amount);
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_ReturnUrl", config.getReturnUrl());
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.put("vnp_Locale", vnp_Locale);
            vnp_Params.put("vnp_BankCode", vnp_BankCode);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String value = vnp_Params.get(fieldName);
                if (value != null && !value.isEmpty()) {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
                    query.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
                }
            }

            String queryUrl = query.substring(0, query.length() - 1);
            String hashRaw = hashData.substring(0, hashData.length() - 1);
            String secureHash = VNPayUtil.hmacSHA512(config.getHashSecret(), hashRaw);

            queryUrl += "&vnp_SecureHash=" + secureHash;

            return config.getPayUrl() + "?" + queryUrl;
        } catch (Exception e) {
            throw new RuntimeException("Không thể thanh toan");
        }
    }



    public boolean validatePayment(Map<String, String> params) {
        String receivedHash = params.remove("vnp_SecureHash");
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (String name : fieldNames) {
            String value = params.get(name);
            if (value != null && !value.isEmpty()) {
                hashData.append(name).append('=').append(value).append('&');
            }
        }

        String finalData = hashData.substring(0, hashData.length() - 1);
        String calculatedHash = VNPayUtil.hmacSHA512(config.getHashSecret(), finalData);

        return calculatedHash.equals(receivedHash);
    }

    public List<PaymentRes> getAllPaymentByCareTaker(Long careTakerId){
        CareTaker careTaker = careTakerRepository.findById(careTakerId).orElseThrow(()->new ApiException(ErrorCode.USER_NOT_FOUND));
        List<Payment> payments = paymentRepository.getAllPaymentOfCareTaker(careTakerId);
        List<PaymentRes> paymentRess = new ArrayList<>();
        for(Payment payment : payments){
            PaymentRes paymentRes = paymentMapper.toPaymentRes(payment);
            paymentRess.add(paymentRes);
        }
        return paymentRess;
    }

    public float getTotalPaymentAmount() {
        Float totalAmount = paymentRepository.getTotalCompletedPaymentAmount();
        return totalAmount != null ? totalAmount : 0f;
    }
}
