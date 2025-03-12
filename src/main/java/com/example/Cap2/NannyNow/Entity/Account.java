package com.example.Cap2.NannyNow.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    Long accountId;

    @Column(name = "user_name")
    String userName;

    @Column(name = "password")
    String password;

    @OneToOne(mappedBy = "account")
    Customer customer;

    @OneToOne(mappedBy = "account")
    Admin admin;

    @OneToOne(mappedBy = "account")
    CareTaker careTaker;

    @OneToMany(mappedBy = "account")
    List<Account_Role> accountRoles;

}
