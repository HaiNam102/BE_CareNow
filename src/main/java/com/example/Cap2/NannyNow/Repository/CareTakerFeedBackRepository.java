package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.CareTakerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareTakerFeedBackRepository extends JpaRepository<CareTakerFeedback,Long> {
    @Query("SELECT cf FROM CareTakerFeedback cf WHERE cf.rating = :rating AND cf.care_taker.care_taker_id = :careTakerId")
    public List<CareTakerFeedback> getFeedBackByRatingAndCareTaker(@Param("rating") int rating,
                                                                   @Param("careTakerId") Long careTakerId);
    
    @Query("SELECT cf FROM CareTakerFeedback cf WHERE cf.care_taker.care_taker_id = :careTakerId")
    public List<CareTakerFeedback> getAllFeedbackByCareTakerId(@Param("careTakerId") Long careTakerId);
    
    @Query("SELECT AVG(cf.rating) FROM CareTakerFeedback cf WHERE cf.care_taker.care_taker_id = :careTakerId")
    public Double getAverageRatingByCareTakerId(@Param("careTakerId") Long careTakerId);
    
    @Query("SELECT COUNT(DISTINCT cf.customer.customer_id) FROM CareTakerFeedback cf WHERE cf.care_taker.care_taker_id = :careTakerId")
    public Integer countDistinctCustomersByCareTakerId(@Param("careTakerId") Long careTakerId);
}
