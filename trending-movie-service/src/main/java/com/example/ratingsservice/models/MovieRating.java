package com.example.ratingsservice.models;

public class MovieRating {
    private String movieId;
    private double rating;

    public MovieRating() {}

    public MovieRating(String movieId, double avgRating) {
        this.movieId = movieId;
        this.rating = avgRating;
    }

    public String getMovieId() { return movieId; }
    public double getRating() { return rating; }

}
