package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByUserName(String userName);
}
