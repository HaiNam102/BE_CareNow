package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByUserName(String userName);

    @Query("SELECT COUNT(a) > 0 FROM Account a " +
            "LEFT JOIN Customer c ON a.id = c.account.id " +
            "LEFT JOIN CareTaker ct ON a.id = ct.account.id " +
            "WHERE LOWER(a.userName) = LOWER(:userName) " +
            "OR LOWER(c.email) = LOWER(:email) " +
            "OR LOWER(ct.email) = LOWER(:email) " +
            "OR c.phoneNumber = :phoneNumber " +
            "OR ct.phoneNumber = :phoneNumber")
    boolean existsByUsernameOrEmailOrPhoneNumber(@Param("userName") String username,
                                                 @Param("email") String email,
                                                 @Param("phoneNumber") String phoneNumber);
}

