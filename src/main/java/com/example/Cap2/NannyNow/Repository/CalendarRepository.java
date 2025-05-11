package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar,Long> {
    @Query("SELECT c FROM Calendar c WHERE c.care_taker.care_taker_id = :careTakerId ORDER BY c.day ASC, c.timeToStart ASC")
    List<Calendar> findByCareTakerId(@Param("careTakerId") Long careTakerId);
}
