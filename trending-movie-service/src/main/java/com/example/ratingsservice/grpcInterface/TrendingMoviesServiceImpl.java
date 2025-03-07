package com.example.ratingsservice.grpcInterface;

import trending.TrendingMoviesServiceGrpc;
import trending.TrendingMoviesRequest;
import trending.TrendingMoviesResponse;
import trending.Movie;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class TrendingMoviesServiceImpl extends TrendingMoviesServiceGrpc.TrendingMoviesServiceImplBase {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void getTopTrendingMovies(TrendingMoviesRequest request, StreamObserver<TrendingMoviesResponse> responseObserver) {
        int limit = request.getLimit() > 0 ? request.getLimit() : 10; // Default to 10 movies

        String sql = "SELECT movie_id, AVG(rating) AS avg_rating " +
                "FROM ratings " +
                "GROUP BY movie_id " +
                "ORDER BY avg_rating DESC " +
                "LIMIT ?";

        List<Movie> movies = jdbcTemplate.query(sql, new Object[]{limit}, movieRowMapper())
                .stream()
                .collect(Collectors.toList());

        TrendingMoviesResponse response = TrendingMoviesResponse.newBuilder()
                .addAllMovies(movies)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private RowMapper<Movie> movieRowMapper() {
        return (rs, rowNum) -> Movie.newBuilder()
                .setId(rs.getString("movie_id"))
                .setTitle("Movie " + rs.getString("movie_id"))
                .setDescription("Trending Movie")
                .setRating(rs.getDouble("avg_rating"))
                .build();
    }
}
