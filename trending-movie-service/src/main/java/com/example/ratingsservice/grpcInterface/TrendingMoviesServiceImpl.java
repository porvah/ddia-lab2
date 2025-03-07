package com.example.ratingsservice.grpcInterface;

import trending.TrendingMoviesServiceGrpc;
import trending.TrendingMoviesRequest;
import trending.TrendingMoviesResponse;
import trending.Movie;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class TrendingMoviesServiceImpl extends TrendingMoviesServiceGrpc.TrendingMoviesServiceImplBase {

    @Autowired
    private JdbcTemplate jdbcTemplate; // For MySQL queries

    @Override
    public void getTopTrendingMovies(TrendingMoviesRequest request, StreamObserver<TrendingMoviesResponse> responseObserver) {

    }
}
