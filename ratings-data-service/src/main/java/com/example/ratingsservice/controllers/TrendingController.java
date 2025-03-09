package com.example.ratingsservice.controllers;

import com.example.ratingsservice.models.MovieRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/trending")
public class TrendingController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/top")
    public List<MovieRating>  getTrending(){
        String sql = "SELECT movie_id, AVG(rating) AS avg_rating " +
                "FROM ratings " +
                "GROUP BY movie_id " +
                "ORDER BY avg_rating DESC " +
                "LIMIT 10";
        List<MovieRating> movies = jdbcTemplate.query(sql, new Object[]{}, (rs, rowNum) -> {
            String movieId = rs.getString("movie_id");
            double avgRating = rs.getDouble("avg_rating");
            return new MovieRating(movieId, avgRating);
        });


        return movies;
    }
}
