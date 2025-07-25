package com.example.Cap2.NannyNow.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "care_recipient")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CareRecipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "care_recipient_id")
    Long careRecipientId;

    @Column(name = "name")
    String name;

    @Column(name = "gender")
    String gender;

    @Column(name = "year_old")
    String yearOld;

    @Column(name = "special_detail", length = 1000)
    String specialDetail;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    Customer customer;
}
