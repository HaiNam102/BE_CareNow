package com.example.Cap2.NannyNow.DTO.Response;

import com.example.Cap2.NannyNow.Enum.EStatusAccount;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRes {
    Long accountId;
    Long userId;
    String nameOfUser;
    String email;
    String image;
    EStatusAccount status;

    public AccountRes(Long accountId, Long userId, String nameOfUser, String email, EStatusAccount status) {
        this.accountId = accountId;
        this.userId = userId;
        this.nameOfUser = nameOfUser;
        this.email = email;
        this.status = status;
    }


}
