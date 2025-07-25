package com.example.Cap2.NannyNow.Entity;

import com.example.Cap2.NannyNow.Enum.ELocationType;
import com.example.Cap2.NannyNow.Enum.EStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "booking")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long bookingId;

    @Column(name = "place_name")
    String placeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type")
    ELocationType locationType;

    @Column(name = "booking_address")
    String bookingAddress;

    @Column(name = "description_place")
    String descriptionPlace;

    @Column(name = "time_to_start")
    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime timeToStart;

    @Column(name = "time_to_end")
    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime timeToEnd;

    @Column(name = "service_progress")
    @Enumerated(EnumType.STRING)
    EStatus serviceProgress;

    @Column(name = "job_description")
    String jobDescription;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "care_taker_id")
    CareTaker care_taker;
    
    @ManyToOne
    @JoinColumn(name = "care_recipient_id")
    CareRecipient careRecipient;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    Payment payment;
    
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    List<BookingDay> bookingDays;
}
