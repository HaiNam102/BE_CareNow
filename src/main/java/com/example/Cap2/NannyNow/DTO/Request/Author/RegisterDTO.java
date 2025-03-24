package com.example.Cap2.NannyNow.DTO.Request.Author;

import com.example.Cap2.NannyNow.DTO.Request.CareRecipientReq;
import com.example.Cap2.NannyNow.Enum.EGender;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterDTO implements Serializable {
    String roleName;
    String nameOfUser;
    String userName;
    String password;
    String email;
    String phoneNumber;
    String district;
    String ward;
    EGender gender;
    Date dob;
    String workableArea;
    String introduceYourself;
    int experienceYear;
    String servicePrice;
    List<Long> selectedOptionDetailIds;
    CareRecipientReq careRecipient;
}
