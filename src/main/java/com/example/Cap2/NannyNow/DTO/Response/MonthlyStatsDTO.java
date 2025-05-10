package com.example.Cap2.NannyNow.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlyStatsDTO {
    int month;
    int year;
    Long transactionCount;
    Double revenue;
}
