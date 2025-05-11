package com.example.Cap2.NannyNow.DTO.Response.CareTaker;

import com.example.Cap2.NannyNow.Entity.OptionDetailsOfCareTaker;
import com.example.Cap2.NannyNow.Entity.OptionsDetails;
import com.example.Cap2.NannyNow.Enum.EGender;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    String gender;
    List<OptionsDetails> optionDetailsOfCareTakers;
}
