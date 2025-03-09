package com.example.movieinfoservice.resources;

import com.example.movieinfoservice.models.CachedMovie;
import com.example.movieinfoservice.models.Movie;
import com.example.movieinfoservice.models.MovieSummary;
import com.example.movieinfoservice.repository.MovieRepository;
import com.example.movieinfoservice.service.MovieService;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @Value("${api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;
    @Autowired
    private final MovieService movieService;
    private final Gson gson = new Gson();
    
    public MovieResource(RestTemplate restTemplate, MovieRepository movieRepository, MovieService movieService) {
        this.restTemplate = restTemplate;
        this.movieRepository = movieRepository;
        this.movieService = movieService;
    }

    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId) {
        // Check if the movie is cached
        Optional<CachedMovie> cachedMovie = movieRepository.findById(movieId);

        if (cachedMovie.isPresent()) {
            CachedMovie movie = cachedMovie.get();
            return new Movie(movie.getId(), movie.getTitle(), movie.getOverview());
        }

        // Fetch from TMDB
        // final String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
        try {
            // Generate random movie JSON
            String movieJson = MovieService.generateRandomMovie(movieId);
            // MovieSummary movieSummary = restTemplate.getForObject(url, MovieSummary.class);
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500));
            MovieSummary movieSummary = gson.fromJson(movieJson, MovieSummary.class);
            if (movieSummary != null) {
                CachedMovie newMovie = new CachedMovie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
                movieRepository.save(newMovie);
                return new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
            }
        } catch (HttpClientErrorException.NotFound | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.SC_NOT_FOUND, "Movie not found", e);
        }
        throw new RuntimeException("Movie not found");
    }
}

