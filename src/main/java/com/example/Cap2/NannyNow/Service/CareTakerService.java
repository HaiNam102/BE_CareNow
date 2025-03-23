package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.CareTakerReq;
import com.example.Cap2.NannyNow.DTO.Response.CareTaker.CareTakerRes;
import com.example.Cap2.NannyNow.DTO.Response.CareTaker.CareTakerSearchRes;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import com.example.Cap2.NannyNow.Entity.CareTakerFeedback;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.CareTakerMapper;
import com.example.Cap2.NannyNow.Repository.CareTakerFeedBackRepository;
import com.example.Cap2.NannyNow.Repository.CareTakerRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CareTakerService {
    CareTakerMapper careTakerMapper;
    CareTakerRepository careTakerRepository;
    CareTakerFeedBackRepository careTakerFeedBackRepository;

    public List<CareTakerRes> getAllCareTaker(){
        List<CareTaker> careTakers = careTakerRepository.findAll();
        List<CareTakerRes> careTakerRes = new ArrayList<>();
        for(CareTaker careTaker : careTakers){
            updateAverageRating(careTaker);
            CareTakerRes careTakerRes1 = careTakerMapper.toCareTakerRes(careTaker);
            careTakerRes1.setTotalReviewers(getTotalReviewers(careTaker.getCare_taker_id()));
            careTakerRes.add(careTakerRes1);
        }
        return careTakerRes;
    }

    public CareTakerRes getCareTakerById(Long id){
        CareTaker careTaker = careTakerRepository.findById(id)
                .orElseThrow(()->new ApiException(ErrorCode.USER_NOT_FOUND));
        updateAverageRating(careTaker);
        CareTakerRes careTakerRes = careTakerMapper.toCareTakerRes(careTaker);
        careTakerRes.setTotalReviewers(getTotalReviewers(careTaker.getCare_taker_id()));
        return careTakerRes;
    }

    public List<CareTakerSearchRes> getCareTakerByDayAndArea(String area, LocalDate dayStart, LocalDate dayEnd){
        List<CareTaker> careTakers = careTakerRepository.getCareTakerByDayAndArea(area, dayStart, dayEnd);
        List<CareTakerSearchRes> careTakerSearchResList = new ArrayList<>();
        
        for(CareTaker careTaker : careTakers){
            updateAverageRating(careTaker);
            CareTakerSearchRes careTakerSearchRes = careTakerMapper.toCareTakerSearchRes(careTaker);
            if (careTaker.getImage() != null) {
                careTakerSearchRes.setImgProfile(careTaker.getImage().getImgProfile());
            } else {
                careTakerSearchRes.setImgProfile(null);
            }
            
            careTakerSearchRes.setTotalReviewers(getTotalReviewers(careTaker.getCare_taker_id()));
            
            careTakerSearchResList.add(careTakerSearchRes);
        }
        
        return careTakerSearchResList;
    }
    
    public int getTotalReviewers(Long careTakerId) {
        Integer count = careTakerFeedBackRepository.countDistinctCustomersByCareTakerId(careTakerId);
        return count != null ? count : 0;
    }
    
    private void updateAverageRating(CareTaker careTaker) {
        Double averageRating = Math.round(careTakerFeedBackRepository.getAverageRatingByCareTakerId(careTaker.getCare_taker_id()) *100)/100.0;
        if (averageRating != null) {
            careTaker.setAvarageRating(averageRating.floatValue());
        } else {
            careTaker.setAvarageRating(0);
        }
        careTakerRepository.save(careTaker);
    }
    
    public float calculateAverageRating(Long careTakerId) {
        List<CareTakerFeedback> feedbacks = careTakerFeedBackRepository.getAllFeedbackByCareTakerId(careTakerId);
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0;
        }
        
        int totalRating = 0;
        for (CareTakerFeedback feedback : feedbacks) {
            totalRating += feedback.getRating();
        }
        
        return (float) totalRating / feedbacks.size();
    }
    
    public CareTakerRes createCareTaker(CareTakerReq careTakerReq) {
        CareTaker careTaker = careTakerMapper.CareTakerReqtoCareTaker(careTakerReq);
        careTaker.setAvarageRating(0);
        CareTaker savedCareTaker = careTakerRepository.save(careTaker);
        return careTakerMapper.toCareTakerRes(savedCareTaker);
    }

    public CareTakerRes updateCareTaker(Long id, CareTakerReq careTakerReq) {
        CareTaker existingCareTaker = careTakerRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        CareTaker updatedCareTaker = careTakerMapper.CareTakerReqtoCareTaker(careTakerReq);
        updatedCareTaker.setCare_taker_id(existingCareTaker.getCare_taker_id());
        updatedCareTaker.setAvarageRating(existingCareTaker.getAvarageRating());
        CareTaker savedCareTaker = careTakerRepository.save(updatedCareTaker);
        return careTakerMapper.toCareTakerRes(savedCareTaker);
    }

    @Transactional
    public void deleteCareTaker(Long id) {
        if (!careTakerRepository.existsById(id)) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }
        careTakerRepository.deleteCareTakerAndRelatedData(id);
    }
}
