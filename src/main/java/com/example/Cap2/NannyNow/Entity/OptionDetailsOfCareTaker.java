package com.example.Cap2.NannyNow.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "option_details_of_care_taker")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OptionDetailsOfCareTaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "option_details_id")
    OptionsDetails optionsDetails;

    @ManyToOne
    @JoinColumn(name = "care_taker_id")
    CareTaker care_taker;
}
