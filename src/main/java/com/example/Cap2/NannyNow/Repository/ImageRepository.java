package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
}
