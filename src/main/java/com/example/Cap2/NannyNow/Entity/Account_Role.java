package com.example.Cap2.NannyNow.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "account_role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account_Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long account_role_id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;
}
