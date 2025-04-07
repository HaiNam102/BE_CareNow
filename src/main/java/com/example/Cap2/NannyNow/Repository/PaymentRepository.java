package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
