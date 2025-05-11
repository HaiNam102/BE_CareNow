package com.example.Cap2.NannyNow.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "option_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OptionsDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long optionDetailsId;

    @Column(name = "detail_name")
    String detailName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "options_id")
    @JsonIgnore
    Options options;

    @OneToMany(mappedBy = "optionsDetails")
    @JsonIgnore
    List<OptionDetailsOfCareTaker> optionDetailsOfCareTaker;

}
