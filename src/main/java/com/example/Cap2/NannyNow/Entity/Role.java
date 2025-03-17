    package com.example.Cap2.NannyNow.Entity;

    import jakarta.persistence.*;
    import lombok.*;
    import lombok.experimental.FieldDefaults;

    import java.util.List;

    @Entity
    @Table(name = "role")
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long role_id;

        @Column(name = "role_name")
        String roleName;

        @OneToMany(mappedBy = "role")
        List<Account_Role> account_role;

    }
