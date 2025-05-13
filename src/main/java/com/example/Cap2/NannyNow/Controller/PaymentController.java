package com.example.Cap2.NannyNow.Controller;

import com.example.Cap2.NannyNow.DTO.Response.ApiResponse;
import com.example.Cap2.NannyNow.Entity.Booking;
import com.example.Cap2.NannyNow.Entity.Payment;
import com.example.Cap2.NannyNow.Exception.SuccessCode;
import com.example.Cap2.NannyNow.Repository.BookingRepository;
import com.example.Cap2.NannyNow.Repository.PaymentRepository;
import com.example.Cap2.NannyNow.Service.PaymentService;
import com.example.Cap2.NannyNow.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    JwtUtil jwtUtil;
    PaymentService paymentService;
    PaymentRepository paymentRepository;

    @PostMapping("/payment")
    public ResponseEntity<?> createPayment(@RequestParam Long bookingId, HttpServletRequest request) {
        String paymentUrl = paymentService.createPaymentUrl(request, bookingId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("paymentUrl", paymentUrl);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/vnpay-return")
    public String handleReturn(@RequestParam Map<String, String> params) {
        boolean valid = paymentService.validatePayment(params);
        if (valid) {
            // Lấy thông tin cần lưu
            String transactionId = params.get("vnp_TxnRef");
            String amountStr = params.get("vnp_Amount");
            String bookingIdStr = params.get("vnp_OrderInfo");

            float price = Float.parseFloat(amountStr) / 100;
            Long bookingId = Long.parseLong(bookingIdStr);

            Payment payment = paymentRepository.findByBooking_BookingId(bookingId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy payment cho bookingId: " + bookingId));

            // Update Payment cũ
            payment.setStatus(true); // đã thanh toán
            payment.setPrice(price);
            payment.setTransactionId(transactionId);
            payment.setUpdateAt(LocalDateTime.now());

            paymentRepository.save(payment);

            return "Thanh toán thành công!";
        } else {
            return "Thanh toán thất bại!";
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPaymentByCaretaker(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long careTakerId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(paymentService.getAllPaymentByCareTaker(careTakerId))
                .build()
        );
    }

    @GetMapping("/total")
    public ResponseEntity<?> getTotalPaymentAmount() {
        float totalAmount = paymentService.getTotalPaymentAmount();
        Map<String, Object> response = new HashMap<>();
        response.put("totalAmount", totalAmount);
        response.put("totalAmountAfterPayCaretaker", totalAmount * 0.7f);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(SuccessCode.GET_SUCCESSFUL.getCode())
                .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                .data(response)
                .build()
        );
    }
}
