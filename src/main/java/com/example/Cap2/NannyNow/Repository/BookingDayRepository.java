package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.BookingDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingDayRepository extends JpaRepository<BookingDay, Long> {
    @Query("SELECT bd FROM BookingDay bd WHERE bd.booking.care_taker.care_taker_id = :careTakerId AND bd.day = :day")
    List<BookingDay> findBookingDaysForCareTakerAndDay(@Param("careTakerId") Long careTakerId, @Param("day") LocalDate day);
    
    @Query("SELECT bd FROM BookingDay bd WHERE bd.booking.bookingId = :bookingId")
    List<BookingDay> findByBookingId(@Param("bookingId") Long bookingId);
} 