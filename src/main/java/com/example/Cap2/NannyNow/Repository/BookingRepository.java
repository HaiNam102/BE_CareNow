package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    @Query("SELECT b FROM Booking b " +
           "JOIN b.bookingDays bd " +
           "WHERE b.care_taker.care_taker_id = :careTakerId AND bd.day = :day " +
           "ORDER BY b.timeToEnd DESC")
    List<Booking> findBookingForDay(@Param("careTakerId") Long careTakerId, @Param("day") LocalDate day);
    
    @Query("SELECT b FROM Booking b WHERE b.care_taker.care_taker_id = :careTakerId")
    List<Booking> findBookingsByCareTakerId(@Param("careTakerId") Long careTakerId);
    
    @Query("SELECT COUNT(DISTINCT b.bookingId) FROM Booking b WHERE b.care_taker.care_taker_id = :careTakerId")
    int countBookingsByCareTakerId(@Param("careTakerId") Long careTakerId);

    @Query("SELECT b FROM Booking b WHERE b.customer.customer_id = :customerId ORDER BY b.bookingId DESC")
    List<Booking> findBookingsByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT b FROM Booking b WHERE b.care_taker.care_taker_id = :careTakerId ORDER BY b.bookingId DESC")
    List<Booking> findByCareTakerId(@Param("careTakerId") Long careTakerId);
}
