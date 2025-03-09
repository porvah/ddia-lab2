package com.moviecatalogservice.controllers;

import com.moviecatalogservice.models.CatalogService;
import com.moviecatalogservice.models.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trending.TrendingMoviesResponse;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {
    @Autowired
    CatalogService catalogService;

    @GetMapping("/trending")
    public List<Movie> getTrendingMovies(@RequestParam(defaultValue = "10") int limit) {
        System.out.println("12");
        TrendingMoviesResponse response = catalogService.getTrendingMovies(limit);
        List<Movie> movies =  response.getMoviesList().stream()
                .map( movie-> new Movie(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getDescription(),
                        movie.getRating()
                ) ).collect(Collectors.toList());
        System.out.println(movies);
        return movies;
    }
}