package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.DTO.Request.CareTakerReq;
import com.example.Cap2.NannyNow.DTO.Response.CareTaker.CareTakerRes;
import com.example.Cap2.NannyNow.DTO.Response.CareTaker.CareTakerSearchRes;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import com.example.Cap2.NannyNow.Exception.ApiException;
import com.example.Cap2.NannyNow.Exception.ErrorCode;
import com.example.Cap2.NannyNow.Mapper.CareTakerMapper;
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

    public List<CareTakerRes> getAllCareTaker(){
        List<CareTaker> careTakers = careTakerRepository.findAll();
        List<CareTakerRes> careTakerRes = new ArrayList<>();
        for(CareTaker careTaker : careTakers){
            CareTakerRes careTakerRes1 = careTakerMapper.toCareTakerRes(careTaker);
            careTakerRes.add(careTakerRes1);
        }
        return careTakerRes;
    }

    public CareTakerRes getCareTakerById(Long id){
        CareTaker careTaker = careTakerRepository.findById(id)
                .orElseThrow(()->new ApiException(ErrorCode.USER_NOT_FOUND));
        CareTakerRes careTakerRes = careTakerMapper.toCareTakerRes(careTaker);
        return careTakerRes;
    }

    public List<CareTakerSearchRes> getCareTakerByDayAndArea(String area, LocalDate dayStart, LocalDate dayEnd){
        List<CareTaker> careTakers = careTakerRepository.getCareTakerByDayAndArea(area, dayStart, dayEnd);
        List<CareTakerSearchRes> careTakerSearchRes = new ArrayList<>();
        for(CareTaker careTaker : careTakers){
            CareTakerSearchRes careTakerSearchRes1 = careTakerMapper.toCareTakerSearchRes(careTaker);
            careTakerSearchRes1.setImgProfile(careTaker.getImage().getImgProfile());
            careTakerSearchRes.add(careTakerSearchRes1);
        }
        return careTakerSearchRes;
    }
    public CareTakerRes createCareTaker(CareTakerReq careTakerReq) {
        CareTaker careTaker = careTakerMapper.CareTakerReqtoCareTaker(careTakerReq);
        CareTaker savedCareTaker = careTakerRepository.save(careTaker);
        return careTakerMapper.toCareTakerRes(savedCareTaker);
    }

    public CareTakerRes updateCareTaker(Long id, CareTakerReq careTakerReq) {
        CareTaker existingCareTaker = careTakerRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        CareTaker updatedCareTaker = careTakerMapper.CareTakerReqtoCareTaker(careTakerReq);
        updatedCareTaker.setCare_taker_id(existingCareTaker.getCare_taker_id());
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
