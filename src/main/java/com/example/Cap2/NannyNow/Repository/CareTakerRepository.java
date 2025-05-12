package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.CareTaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CareTakerRepository extends JpaRepository<CareTaker,Long> {

    @Query("SELECT c FROM CareTaker c " +
            "JOIN c.calendars cal " +
            "WHERE c.district LIKE %:area% " +
            "AND cal.day BETWEEN :dayStart AND :dayEnd")
    public List<CareTaker> getCareTakerByDayAndArea(@Param("area") String area, @Param("dayStart") LocalDate dayStart, @Param("dayEnd") LocalDate dayEnd);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE p, b, c
        FROM care_taker c
        LEFT JOIN booking b ON c.care_taker_id = b.care_taker_id
        LEFT JOIN payment p ON b.booking_id = p.booking_id
        WHERE c.care_taker_id = :careTakerId
        """, nativeQuery = true)
    void deleteCareTakerAndRelatedData(@Param("careTakerId") Long careTakerId);

    @Query("SELECT c.email FROM CareTaker c WHERE c.care_taker_id = :careTakerId")
    String findEmailById(@Param("careTakerId") Long careTakerId);

    @Query("SELECT c.nameOfCareTaker FROM CareTaker c WHERE c.care_taker_id = :careTakerId")
    String findNameById(@Param("careTakerId") Long careTakerId);
}
