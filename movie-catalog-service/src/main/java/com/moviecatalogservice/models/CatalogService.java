package com.moviecatalogservice.models;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import trending.TrendingMoviesServiceGrpc;
import trending.TrendingMoviesRequest;
import trending.TrendingMoviesResponse;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {
    private final TrendingMoviesServiceGrpc.TrendingMoviesServiceBlockingStub trendingMoviesService;
    private final ManagedChannel channel;

    public CatalogService() {
        // Connect to gRPC server
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9091) // gRPC Server port
                .usePlaintext()
                .build();
        this.trendingMoviesService = TrendingMoviesServiceGrpc.newBlockingStub(channel);
    }

    public TrendingMoviesResponse getTrendingMovies(int limit) {
        TrendingMoviesRequest request = TrendingMoviesRequest.newBuilder()
                .setLimit(limit)
                .build();
        return trendingMoviesService.getTopTrendingMovies(request);
    }

    // Graceful shutdown of the channel
    public void shutdown() {
        channel.shutdown();
    }
}
