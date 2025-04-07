package com.example.Cap2.NannyNow.DTO.Response.CareTaker;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

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
}
