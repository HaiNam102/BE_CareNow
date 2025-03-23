package com.example.Cap2.NannyNow.DTO.Response.CareTaker;

import com.example.Cap2.NannyNow.Enum.EGender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CareTakerRes {
    String nameOfCareTaker;
    int experienceYear;
    String servicePrice;
    String introduceYourself;
    String workableArea;
    String ward;
    String district;
    float rating;
    int totalReviewers;
}
