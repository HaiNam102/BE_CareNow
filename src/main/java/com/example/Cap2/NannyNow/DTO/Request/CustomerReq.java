package com.example.Cap2.NannyNow.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerReq {
    String nameOfCustomer;
    String email;
    String phoneNumber;
    String city;
    String address;
}
