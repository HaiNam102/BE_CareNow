package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.CareTaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CareTakerRepository extends JpaRepository<CareTaker,Long> {

    @Query("SELECT c FROM CareTaker c " +
            "JOIN c.calendars cal " +
            "WHERE c.city LIKE %:area% " +
            "AND cal.day BETWEEN :dayStart AND :dayEnd")
    public List<CareTaker> getCareTakerByDayAndArea(@Param("area") String area, @Param("dayStart") LocalDate dayStart, @Param("dayEnd") LocalDate dayEnd);
}
