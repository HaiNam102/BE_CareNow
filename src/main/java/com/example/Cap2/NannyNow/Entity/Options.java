package com.example.Cap2.NannyNow.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "options")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long optionsId;

    @Column(name = "name_option")
    String nameOption;

    @OneToMany(mappedBy = "options")
    List<OptionsDetails> optionsDetails;
}
