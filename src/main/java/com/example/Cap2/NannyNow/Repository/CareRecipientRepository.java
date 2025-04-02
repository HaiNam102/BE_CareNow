package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.CareRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CareRecipientRepository extends JpaRepository<CareRecipient,Long> {
    @Query("SELECT cr FROM CareRecipient cr WHERE cr.customer.customer_id = :customerId")
    CareRecipient getCareRecipientByCustomerId(@Param("customerId") Long customerId);
}
