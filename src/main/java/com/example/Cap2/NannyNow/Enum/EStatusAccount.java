package com.example.Cap2.NannyNow.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum EStatusAccount {
    PENDING("cho duyet"),
    ACTIVE("dang hoat dong"),
    INACTIVE("khong hoat dong");

    String status;
}
