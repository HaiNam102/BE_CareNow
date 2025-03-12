package com.example.Cap2.NannyNow.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum EGender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");
    String name;
}
