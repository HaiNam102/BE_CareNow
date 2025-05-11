package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.OptionsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionsDetailsRepository extends JpaRepository<OptionsDetails,Long> {
    @Query("SELECT od FROM OptionsDetails od LEFT JOIN FETCH od.options")
    List<OptionsDetails> findAllWithOptions();

    @Query("SELECT od FROM OptionsDetails od LEFT JOIN FETCH od.options o WHERE o.optionsId = :optionsId")
    List<OptionsDetails> findAllByOptionsId(@Param("optionsId") Long optionsId);
}
