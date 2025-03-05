package com.example.Cap2.NannyNow.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "care_taker_feedback")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CareTakerFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long feedbackId;

    @Column(name = "feedback")
    String feedback;

    @Column(name = "rating")
    Integer rating;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "care_taker_id")
    CareTaker careTaker;
}
