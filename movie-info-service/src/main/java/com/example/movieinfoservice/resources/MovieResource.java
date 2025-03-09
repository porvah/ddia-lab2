package com.example.movieinfoservice.resources;

import com.example.movieinfoservice.models.CachedMovie;
import com.example.movieinfoservice.models.Movie;
import com.example.movieinfoservice.models.MovieSummary;
import com.example.movieinfoservice.repository.MovieRepository;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @Value("${api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;

    public MovieResource(RestTemplate restTemplate, MovieRepository movieRepository) {
        this.restTemplate = restTemplate;
        this.movieRepository = movieRepository;
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
        final String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
        try {
            MovieSummary movieSummary = restTemplate.getForObject(url, MovieSummary.class);
            if (movieSummary != null) {
                CachedMovie newMovie = new CachedMovie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
                movieRepository.save(newMovie);
                return new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.SC_NOT_FOUND, "Movie not found", e);
        }
        throw new RuntimeException("Movie not found");
    }
}

