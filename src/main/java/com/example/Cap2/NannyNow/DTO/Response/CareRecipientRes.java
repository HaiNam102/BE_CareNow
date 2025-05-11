package com.example.Cap2.NannyNow.DTO.Response;

import com.example.Cap2.NannyNow.Enum.EGender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CareRecipientRes {
    Long careRecipientId;
    String name;
    EGender gender;
    String yearOld;
    String specialDetail;
}
