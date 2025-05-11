package com.example.Cap2.NannyNow.DTO.Response.Options;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OptionDetailsOfCareTakerRes {
    Long id;
    Long optionDetailsId;
    Long careTakerId;
}