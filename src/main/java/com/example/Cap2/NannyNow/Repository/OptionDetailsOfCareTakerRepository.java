package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.OptionDetailsOfCareTaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionDetailsOfCareTakerRepository extends JpaRepository<OptionDetailsOfCareTaker,Long> {
}
