package com.example.Cap2.NannyNow.DTO.Request.Author;

import com.example.Cap2.NannyNow.Enum.EGender;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterDTO implements Serializable {
    String roleName;
    String nameOfUser;
    String userName;
    String password;
    String email;
    String phoneNumber;
    String city;
    String address;
    EGender gender;
    Date dob;
    String workableArea;
    int experience_year;
    String salary;
    float avarageRating;
    boolean trainingStatus;
}
