package com.example.Cap2.NannyNow.DTO.Response.CareTaker;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CareTakerSearchRes {
    Long careTakerId;
    String nameOfCareTaker;
    int experienceYear;
    String imgProfile;
    String ward;
    String district;
    float rating;
    int totalReviewers;
    int totalBookings;
    String servicePrice;
}
