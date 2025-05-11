package com.example.Cap2.NannyNow.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "calendar")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long calendarId;

    @Column(name = "day")
    LocalDate day;

    @Column(name = "time_to_start")
    LocalTime timeToStart;

    @Column(name = "time_to_end")
    LocalTime timeToEnd;

    @ManyToOne
    @JoinColumn(name = "care_taker_id")
    CareTaker care_taker;
}
