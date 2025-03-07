package com.example.ratingsservice.grpcInterface;


import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class TrendingMoviesServiceImpl extends TrendingMoviesServiceGrpc.TrendingMoviesServiceImplBase {

    @Autowired
    private JdbcTemplate jdbcTemplate; // For MySQL queries

    @Autowired
    private MongoTemplate mongoTemplate; // For MongoDB queries

    @Override
    public void getTopTrendingMovies(TrendingMoviesRequest request, StreamObserver<TrendingMoviesResponse> responseObserver) {
        int limit = request.getLimit() == 0 ? 10 : request.getLimit(); // Default limit is 10

        // Step 1: Get top 10 movies from MySQL
        List<String> topMovieIds = jdbcTemplate.queryForList(
                "SELECT movie_id FROM ratings ORDER BY avg_rating DESC LIMIT ?",
                new Object[]{limit}, String.class);

        // Step 2: Fetch movie details from MongoDB
        List<Movie> movies = topMovieIds.stream().map(movieId -> {
            MovieDocument movieDoc = mongoTemplate.findById(movieId, MovieDocument.class);
            if (movieDoc != null) {
                return Movie.newBuilder()
                        .setId(movieDoc.getId())
                        .setTitle(movieDoc.getTitle())
                        .setDescription(movieDoc.getDescription())
                        .setRating(movieDoc.getAvgRating())
                        .build();
            }
            return null;
        }).filter(movie -> movie != null).collect(Collectors.toList());

        // Step 3: Send response
        TrendingMoviesResponse response = TrendingMoviesResponse.newBuilder()
                .addAllMovies(movies)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
