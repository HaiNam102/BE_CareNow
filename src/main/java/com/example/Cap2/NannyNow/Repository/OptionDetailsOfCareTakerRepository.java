package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.OptionDetailsOfCareTaker;
import com.example.Cap2.NannyNow.Entity.OptionsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionDetailsOfCareTakerRepository extends JpaRepository<OptionDetailsOfCareTaker,Long> {
    @Query("SELECT c.optionsDetails FROM OptionDetailsOfCareTaker c WHERE c.care_taker.care_taker_id = :careTakerId")
    List<OptionsDetails> findOptionDetailsIdByCareTakerId(@Param("careTakerId") Long careTakerId);
}
