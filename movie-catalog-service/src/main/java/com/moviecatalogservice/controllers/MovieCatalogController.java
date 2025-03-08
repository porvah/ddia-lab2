package com.moviecatalogservice.controllers;

import com.moviecatalogservice.models.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trending.TrendingMoviesResponse;

@CrossOrigin
@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {
    @Autowired
    CatalogService catalogService;

    @GetMapping("/trending")
    public TrendingMoviesResponse getTrendingMovies(@RequestParam(defaultValue = "10") int limit) {
        System.out.println("12");
        System.out.println(catalogService.getTrendingMovies(limit));
        return catalogService.getTrendingMovies(limit);
    }
}