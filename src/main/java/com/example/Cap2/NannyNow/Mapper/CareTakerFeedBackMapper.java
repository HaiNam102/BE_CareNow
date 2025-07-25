package com.example.Cap2.NannyNow.Mapper;

import com.example.Cap2.NannyNow.DTO.Request.CareTakerFeedBackReq;
import com.example.Cap2.NannyNow.DTO.Response.CareTakerFeedBackRes;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import com.example.Cap2.NannyNow.Entity.CareTakerFeedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CareTakerFeedBackMapper {
    CareTakerFeedBackMapper INSTANCE = Mappers.getMapper(CareTakerFeedBackMapper.class);

    CareTakerFeedback toCareTakerFeedBack(CareTakerFeedBackReq careTakerFeedBackReq);

    @Mapping(source = "customer.nameOfCustomer", target = "customerName")
    @Mapping(source = "feedback", target = "feedback")
    CareTakerFeedBackRes toCareTakerFeedBackRes(CareTakerFeedback careTakerFeedback);
}
