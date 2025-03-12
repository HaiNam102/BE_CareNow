package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.Account_Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Account_RoleRepository extends JpaRepository<Account_Role,Long> {
}
