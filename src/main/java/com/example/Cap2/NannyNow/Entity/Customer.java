package com.example.Cap2.NannyNow.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long customer_id;

    @Column(name = "name_of_customer")
    String nameOfCustomer;

    @Column(name = "email")
    String email;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "city")
    String city;

    @Column(name = "address")
    String address;

    @OneToOne
    @JoinColumn(name = "account_id")
    Account account;

    @OneToMany(mappedBy = "customer")
    List<CareTakerFeedback> careTakerFeedbacks;

    @OneToOne(mappedBy = "customer")
    CareRecipient careRecipient;

    @OneToMany(mappedBy = "customer")
    List<Booking> bookings;
}
