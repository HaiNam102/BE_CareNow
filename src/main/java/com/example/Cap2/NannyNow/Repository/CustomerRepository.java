package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
//    @Query("SELECT COUNT(c) FROM Customer c WHERE LOWER(username) = LOWER(:username) OR LOWER(email) = LOWER(:email) OR LOWER(phoneNumber) = LOWER(:phoneNumber)")
//    boolean checkExistCustomer(String username,String email,String phoneNumber);
    @Modifying
    @Transactional
    @Query(value = """
        DELETE p, b, c 
        FROM customer c
        LEFT JOIN booking b ON c.customer_id = b.customer_id
        LEFT JOIN payment p ON b.booking_id = p.booking_id
        WHERE c.customer_id = :customerId
        """, nativeQuery = true)
    void deleteCustomerAndRelatedData(@Param("customerId") Long customerId);
}
