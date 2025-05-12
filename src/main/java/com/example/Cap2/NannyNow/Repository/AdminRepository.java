package com.example.Cap2.NannyNow.Repository;

import com.example.Cap2.NannyNow.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    @Query("SELECT a.email FROM Admin a")
    List<String> findAllAdminEmails();
} 