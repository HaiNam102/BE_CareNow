package com.example.Cap2.NannyNow.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "image")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long image_id;

    @Column(name = "img_profile")
    String imgProfile;

    @Column(name = "img_certificate")
    String imgCertificate;

    @Column(name = "img_cccd")
    String imgCccd;
}
