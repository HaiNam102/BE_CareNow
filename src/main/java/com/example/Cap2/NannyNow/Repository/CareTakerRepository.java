package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.CareTaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CareTakerRepository extends JpaRepository<CareTaker,Long> {
//    @Query("SELECT COUNT(c) FROM CareTaker c WHERE LOWER(username) = LOWER(:username) OR LOWER(email) = LOWER(:email) OR LOWER(phoneNumber) = LOWER(:phoneNumber)")
//    boolean checkExistCareTaker(String username,String email,String phoneNumber);
}
