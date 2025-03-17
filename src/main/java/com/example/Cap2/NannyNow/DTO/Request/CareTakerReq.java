package com.example.Cap2.NannyNow.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CareTakerReq {
    String nameOfCareTaker;
    String email;
    String phoneNumber;
    String introduceYourself;
    Date dob;
    String gender;
    String city;
    String workableArea;
    int experienceYear;
    int salary;
}
