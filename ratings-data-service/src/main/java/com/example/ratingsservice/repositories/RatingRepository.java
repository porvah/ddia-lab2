package com.example.ratingsservice.repositories;

import com.example.ratingsservice.models.RatingEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<RatingEntry, Long> {

    List<RatingEntry> findByUserId(String userId);
}