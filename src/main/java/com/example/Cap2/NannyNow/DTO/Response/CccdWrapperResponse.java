package com.example.Cap2.NannyNow.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CccdWrapperResponse {
    int errorCode;
    String errorMessage;
    List<CccdResponse> data;
}
