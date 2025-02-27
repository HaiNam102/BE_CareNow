package com.example.Cap2.NannyNow.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    Long user_id;

    @Column(name = "name_of_user")
    String nameOfUser;

    @Column(name = "email")
    String email;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "city")
    String city;

    @Column(name = "address")
    String address;

    @Column(name = "special_request")
    String special_request;

    @OneToOne
    @JoinColumn(name = "account_id")
    Account account;

}
