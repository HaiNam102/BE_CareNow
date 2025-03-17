package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.CareTakerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareTakerFeedBackRepository extends JpaRepository<CareTakerFeedback,Long> {
    @Query("SELECT cf FROM CareTakerFeedback cf WHERE cf.rating = :rating AND cf.care_taker.id = :careTakerId")
    public List<CareTakerFeedback> getFeedBackByRatingAndCareTaker(@Param("rating") int rating,
                                                                   @Param("careTakerId") Long careTakerId);
}
