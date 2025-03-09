package com.example.ratingsservice.services;

import com.example.ratingsservice.models.Rating;
import com.example.ratingsservice.models.RatingEntry;
import com.example.ratingsservice.models.UserRating;
import com.example.ratingsservice.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public UserRating getRatingsByUserId(String userId) {
        
        List<RatingEntry> ratingEntries = ratingRepository.findByUserId(userId);

        
        List<Rating> ratings = ratingEntries.stream()
                .map(r -> new Rating(r.getMovieId(), r.getRating()))
                .collect(Collectors.toList());

        return new UserRating(ratings);
    }
}