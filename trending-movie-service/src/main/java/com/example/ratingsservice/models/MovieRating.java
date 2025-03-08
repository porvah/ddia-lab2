package com.example.ratingsservice.models;

public class MovieRating {
    private String movieId;
    private double avgRating;

    public MovieRating() {}

    public MovieRating(String movieId, double avgRating) {
        this.movieId = movieId;
        this.avgRating = avgRating;
    }

    public String getMovieId() { return movieId; }
    public double getRating() { return avgRating; }

}
