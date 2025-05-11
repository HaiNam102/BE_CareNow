package com.example.Cap2.NannyNow.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "district")
    String district;

    @Column(name = "ward")
    String ward;

    @OneToOne(cascade = CascadeType.REMOVE)  // Delete account when customer is deleted
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)  // Remove feedbacks on customer delete
    @JsonIgnore
    List<CareTakerFeedback> careTakerFeedbacks;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)  // Remove care recipients when customer is deleted
    @JsonIgnoreProperties("customer")
    List<CareRecipient> careRecipients;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)  // Remove bookings when customer is deleted
    @JsonIgnore
    List<Booking> bookings;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    List<ChatRoom> chatRooms;

}
