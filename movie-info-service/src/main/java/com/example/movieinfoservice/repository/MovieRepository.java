package com.example.movieinfoservice.repository;

import com.example.movieinfoservice.models.CachedMovie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends MongoRepository<CachedMovie, String> {
}
