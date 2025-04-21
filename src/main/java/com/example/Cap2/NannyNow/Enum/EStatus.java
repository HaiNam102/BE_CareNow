package com.example.Cap2.NannyNow.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum EStatus {
    PENDING("dang cho"),
    ACCEPT("chap nhan"),
    REJECT("tu choi"),
    COMPLETED("hoan thanh");

    String status;
}
