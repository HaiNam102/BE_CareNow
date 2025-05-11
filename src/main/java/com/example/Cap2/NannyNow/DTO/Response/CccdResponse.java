package com.example.Cap2.NannyNow.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CccdResponse {
    String id;
    String id_prob;
    String name;
    String name_prob;
    String dob;
    String dob_prob;
    String sex;
    String sex_prob;
    String nationality;
    String nationality_prob;
    String home;
    String home_prob;
    String address;
    String address_prob;
    String doe;
    String doe_prob;
    String overall_score;
    String number_of_name_lines;
    AddressEntities address_entities;
    String type_new;
    String type;
}
