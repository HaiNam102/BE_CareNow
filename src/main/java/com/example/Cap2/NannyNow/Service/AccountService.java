package com.example.Cap2.NannyNow.Service;

import com.example.Cap2.NannyNow.Entity.Account;
import com.example.Cap2.NannyNow.Repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
    AccountRepository accountRepository;

    public Account getAccountByUserName(String userName){
        return this.accountRepository.findByUserName(userName);
    }
}
