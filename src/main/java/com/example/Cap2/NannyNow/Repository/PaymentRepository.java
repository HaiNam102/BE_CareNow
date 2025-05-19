package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.DTO.Response.PaymentRes;
import com.example.Cap2.NannyNow.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Query("SELECT p FROM Payment p WHERE p.booking.care_taker.care_taker_id = :careTakerId ORDER BY p.paymentId DESC")
    List<Payment> getAllPaymentOfCareTaker(Long careTakerId);

    @Query("SELECT p FROM Payment p WHERE p.booking.bookingId = :bookingId ORDER BY p.paymentId DESC")
    Optional<Payment> findByBooking_BookingId(Long bookingId);
    
    @Query("SELECT SUM(p.price) FROM Payment p WHERE p.status = true")
    Float getTotalCompletedPaymentAmount();

    @Query("SELECT p FROM Payment p ORDER BY p.paymentId DESC")
    List<Payment> getAllPayment();
}
