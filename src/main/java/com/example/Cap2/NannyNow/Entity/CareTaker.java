package com.example.Cap2.NannyNow.Entity;

import com.example.Cap2.NannyNow.Enum.EGender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "care_taker")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CareTaker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long care_taker_id;

    @Column(name = "name_of_care_taker")
    String nameOfCareTaker;

    @Column(name = "email")
    String email;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "introduce_yourself")
    String introduceYourself;

    @Column(name = "dob")
    Date dob;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    EGender gender;

    @Column(name = "city")
    String city;

    @Column(name = "workable_area")
    String workableArea;

    @Column(name = "experience_year")
    int experienceYear;

    @Column(name = "salary")
    int salary;

    @Column(name = "avarage_rating")
    float avarageRating;

    @Column(name = "training_status")
    boolean trainingStatus;

    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;

    @OneToOne(mappedBy = "care_taker")
    @JsonIgnore
    Image image;

    @OneToMany(mappedBy = "care_taker")
    @JsonIgnore
    List<Calendar> calendars;

    @OneToMany(mappedBy = "care_taker")
    @JsonIgnore
    List<OptionOfCareTaker> optionOfCareTakers;

    @OneToMany(mappedBy = "care_taker")
    @JsonIgnore
    List<CareTakerFeedback> careTakerFeedbacks;

    @OneToMany(mappedBy = "care_taker")
    @JsonIgnore
    List<Booking> bookings;
}
