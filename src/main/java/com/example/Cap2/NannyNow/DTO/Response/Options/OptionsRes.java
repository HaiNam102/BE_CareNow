package com.example.Cap2.NannyNow.DTO.Response.Options;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OptionsRes {
    Long optionsId;
    String nameOption;
}
