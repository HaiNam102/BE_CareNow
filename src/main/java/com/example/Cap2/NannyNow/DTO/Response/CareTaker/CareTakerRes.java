package com.example.Cap2.NannyNow.DTO.Response.CareTaker;

import com.example.Cap2.NannyNow.DTO.Response.CareTakerFeedBackRes;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CareTakerRes {
    Long careTakerId;
    String nameOfCareTaker;
    int experienceYear;
    String servicePrice;
    String introduceYourself;
    String workableArea;
    String ward;
    String district;
    float rating;
    int numberOfReviews;
    int totalBookings;
    List<CareTakerFeedBackRes> careTakerFeedBackRes;
}
