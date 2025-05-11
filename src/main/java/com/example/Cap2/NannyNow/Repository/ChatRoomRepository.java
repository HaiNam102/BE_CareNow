package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.ChatRoom;
import com.example.Cap2.NannyNow.Entity.CareTaker;
import com.example.Cap2.NannyNow.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 1) by Customer object + CareTaker object
    @Query("""
      SELECT cr
        FROM ChatRoom cr
       WHERE cr.customer     = :customer
         AND cr.care_taker   = :careTaker
    """)
    Optional<ChatRoom> findByCustomerAndCareTaker(
        @Param("customer")   Customer customer,
        @Param("careTaker")  CareTaker careTaker
    );

    // 2) all rooms for given customer_id field on Customer
    @Query("""
      SELECT cr
        FROM ChatRoom cr
       WHERE cr.customer.customer_id = :customerId
    """)
    List<ChatRoom> findByCustomerId(
        @Param("customerId") Long customerId
    );

    // 3) all rooms for given care_taker_id field on CareTaker
    @Query("""
      SELECT cr
        FROM ChatRoom cr
       WHERE cr.care_taker.care_taker_id = :careTakerId
    """)
    List<ChatRoom> findByCareTakerId(
        @Param("careTakerId") Long careTakerId
    );
}
