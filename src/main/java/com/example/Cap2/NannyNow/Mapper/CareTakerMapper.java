package com.example.Cap2.NannyNow.Mapper;

import com.example.Cap2.NannyNow.DTO.Request.Author.RegisterDTO;
import com.example.Cap2.NannyNow.DTO.Request.CareTakerReq;
import com.example.Cap2.NannyNow.DTO.Response.CareTaker.CareTakerRes;
import com.example.Cap2.NannyNow.DTO.Response.CareTaker.CareTakerSearchRes;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = CareTakerFeedBackMapper.class)
public interface CareTakerMapper {
    CareTakerMapper INSTANCE = Mappers.getMapper(CareTakerMapper.class);
    @Mapping(source = "nameOfUser" , target = "nameOfCareTaker")
    CareTaker toCareTaker(RegisterDTO registerDTO);

    @Mapping(source = "avarageRating", target = "rating")
    @Mapping(source = "care_taker_id",target = "careTakerId")
    @Mapping(source = "careTakerFeedbacks",target = "careTakerFeedBackRes")
    @Mapping(source = "image.imgProfile",target = "image")
    CareTakerRes toCareTakerRes(CareTaker careTaker);

    @Mapping(source = "avarageRating", target = "rating")
    @Mapping(source = "care_taker_id",target = "careTakerId")
    @Mapping(source = "gender",target = "gender")
    CareTakerSearchRes toCareTakerSearchRes(CareTaker careTaker);

    CareTaker CareTakerReqtoCareTaker(CareTakerReq careTakerReq);
}
