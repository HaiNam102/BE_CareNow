package com.example.Cap2.NannyNow.DTO.Response.Options;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OptionsDetailsRes {
    Long optionDetailsId;
    String detailName;
    Long optionsId;  // This field will now be properly populated
}

