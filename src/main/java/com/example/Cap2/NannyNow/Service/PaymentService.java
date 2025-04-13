package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Response.CareTakerFeedBackRes;
import com.example.Cap2.NannyNow.DTO.Response.PaymentRes;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import com.example.Cap2.NannyNow.Entity.Payment;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.PaymentMapper;
import com.example.Cap2.NannyNow.Repository.CareTakerRepository;
import com.example.Cap2.NannyNow.Repository.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    PaymentMapper paymentMapper;
    PaymentRepository paymentRepository;
    CareTakerRepository careTakerRepository;

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

}
