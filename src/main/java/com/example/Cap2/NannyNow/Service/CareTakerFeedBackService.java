package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.CareTakerFeedBackReq;
import com.example.Cap2.NannyNow.DTO.Response.CareTaker.CareTakerRes;
import com.example.Cap2.NannyNow.DTO.Response.CareTakerFeedBackRes;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import com.example.Cap2.NannyNow.Entity.CareTakerFeedback;
import com.example.Cap2.NannyNow.Entity.Customer;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.CareTakerFeedBackMapper;
import com.example.Cap2.NannyNow.Repository.CareTakerFeedBackRepository;
import com.example.Cap2.NannyNow.Repository.CareTakerRepository;
import com.example.Cap2.NannyNow.Repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CareTakerFeedBackService {
    CareTakerFeedBackRepository careTakerFeedBackRepository;
    CustomerRepository customerRepository;
    CareTakerRepository careTakerRepository;
    CareTakerFeedBackMapper careTakerFeedBackMapper;

    @Transactional
    public CareTakerFeedback saveCareTakerFeedback(CareTakerFeedBackReq careTakerFeedBackReq, Long customer_id, Long careTaker_id){
        Customer customer = customerRepository.findById(customer_id).orElseThrow(()->new ApiException(ErrorCode.USER_NOT_FOUND));
        CareTaker careTaker = careTakerRepository.findById(careTaker_id).orElseThrow(()->new ApiException((ErrorCode.USER_NOT_FOUND)));
        CareTakerFeedback careTakerFeedback = careTakerFeedBackMapper.toCareTakerFeedBack(careTakerFeedBackReq);
        careTakerFeedback.setCustomer(customer);
        careTakerFeedback.setCare_taker(careTaker);
        careTakerFeedback.setCreatedAt(LocalDate.from(LocalDateTime.now()));
        careTakerFeedBackRepository.save(careTakerFeedback);
        return careTakerFeedback;
    }

    public List<CareTakerFeedBackRes> getFeedBackByRatingAndCareTaker(int rating,Long careTakerId){
        CareTaker careTaker = careTakerRepository.findById(careTakerId).orElseThrow(()->new ApiException((ErrorCode.USER_NOT_FOUND)));
        List<CareTakerFeedback> careTakerFeedbacks = careTakerFeedBackRepository.getFeedBackByRatingAndCareTaker(rating, careTaker.getCare_taker_id());
        List<CareTakerFeedBackRes> careTakerFeedBackRes = new ArrayList<>();
        for(CareTakerFeedback careTakerFeedback : careTakerFeedbacks){
            CareTakerFeedBackRes careTakerFeedBackRes1 = careTakerFeedBackMapper.toCareTakerFeedBackRes(careTakerFeedback);
            careTakerFeedBackRes.add(careTakerFeedBackRes1);
        }
        return careTakerFeedBackRes;
    }
}
