package com.example.ratingsservice.grpcInterface;

import com.example.ratingsservice.models.MovieDetails;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import trending.TrendingMoviesServiceGrpc;
import trending.TrendingMoviesRequest;
import trending.TrendingMoviesResponse;
import trending.Movie;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.stream.Collectors;

@GRpcService
public class TrendingMoviesServiceImpl extends TrendingMoviesServiceGrpc.TrendingMoviesServiceImplBase {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${movie.service.url}")
    private String movieServiceUrl;

    @Override
    public void getTopTrendingMovies(TrendingMoviesRequest request, StreamObserver<TrendingMoviesResponse> responseObserver) {
        int limit = request.getLimit() > 0 ? request.getLimit() : 10; // Default to 10 movies

        String sql = "SELECT movie_id, AVG(rating) AS avg_rating " +
                "FROM ratings " +
                "GROUP BY movie_id " +
                "ORDER BY avg_rating DESC " +
                "LIMIT ?";

        List<Movie> movies = jdbcTemplate.query(sql, new Object[]{limit}, (rs, rowNum) -> {
            String movieId = rs.getString("movie_id");
            double avgRating = rs.getDouble("avg_rating");

            // Fetch movie details from MovieResource
            MovieDetails movieDetails = fetchMovieDetails(movieId);

            return Movie.newBuilder()
                    .setId(movieId)
                    .setTitle(movieDetails.getName()) // Use `name` instead of `title`
                    .setDescription(movieDetails.getDescription())
                    .setRating(avgRating)
                    .build();
        });

        TrendingMoviesResponse response = TrendingMoviesResponse.newBuilder()
                .addAllMovies(movies)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private MovieDetails fetchMovieDetails(String movieId) {
        try {
            String url = movieServiceUrl + "/movies/" + movieId;
            return restTemplate.getForObject(url, MovieDetails.class);
        } catch (Exception e) {
            return new MovieDetails("Unknown Title", "No description");
        }
    }
}


