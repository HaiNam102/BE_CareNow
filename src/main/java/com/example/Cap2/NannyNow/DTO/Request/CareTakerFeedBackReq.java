package com.example.Cap2.NannyNow.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CareTakerFeedBackReq {
    String feedback;
    int rating;
}
