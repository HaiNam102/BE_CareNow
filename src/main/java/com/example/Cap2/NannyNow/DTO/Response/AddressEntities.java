package com.example.Cap2.NannyNow.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressEntities {
    String province;
    String district;
    String ward;
    String street;
}
