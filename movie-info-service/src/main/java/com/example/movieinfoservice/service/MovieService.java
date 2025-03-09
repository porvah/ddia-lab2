package com.example.movieinfoservice.service;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private static final Random random = new Random();
    private static final Faker faker = new Faker();
    private static final Gson gson = new Gson();

    public static String generateRandomMovie(String id) {
        JsonObject movie = new JsonObject();
        
        movie.addProperty("adult", false);
        movie.addProperty("backdrop_path", "/randomBackdrop" + random.nextInt(100) + ".jpg");
        movie.add("belongs_to_collection", null);
        movie.addProperty("budget", random.nextInt(100_000_000));  // Random budget up to 100M
        
        JsonArray genres = new JsonArray();
        JsonObject genre = new JsonObject();
        genre.addProperty("id", random.nextInt(50));
        genre.addProperty("name", faker.book().genre());
        genres.add(genre);
        movie.add("genres", genres);
        
        movie.addProperty("homepage", "");
        movie.addProperty("id", id);
        movie.addProperty("imdb_id", "tt" + (1000000 + random.nextInt(9000000)));
        
        JsonArray originCountries = new JsonArray();
        originCountries.add("US");
        originCountries.add("GB");
        movie.add("origin_country", originCountries);
        
        movie.addProperty("original_language", "en");
        movie.addProperty("original_title", faker.book().title());
        movie.addProperty("overview", faker.lorem().sentence());
        movie.addProperty("popularity", random.nextDouble() * 10);
        movie.addProperty("poster_path", "/randomPoster" + random.nextInt(100) + ".jpg");
        
        JsonArray productionCompanies = new JsonArray();
        JsonObject company = new JsonObject();
        company.addProperty("id", random.nextInt(10000));
        company.add("logo_path", JsonNull.INSTANCE);
        company.addProperty("name", faker.company().name());
        company.addProperty("origin_country", "");
        productionCompanies.add(company);
        movie.add("production_companies", productionCompanies);
        
        JsonArray productionCountries = new JsonArray();
        JsonObject country = new JsonObject();
        country.addProperty("iso_3166_1", "US");
        country.addProperty("name", "United States of America");
        productionCountries.add(country);
        movie.add("production_countries", productionCountries);
        
        movie.addProperty("release_date", "2023-" + (1 + random.nextInt(12)) + "-" + (1 + random.nextInt(28)));
        movie.addProperty("revenue", random.nextInt(500_000_000));
        movie.addProperty("runtime", 90 + random.nextInt(60));
        
        JsonArray spokenLanguages = new JsonArray();
        JsonObject language = new JsonObject();
        language.addProperty("english_name", "English");
        language.addProperty("iso_639_1", "en");
        language.addProperty("name", "English");
        spokenLanguages.add(language);
        movie.add("spoken_languages", spokenLanguages);
        
        movie.addProperty("status", "Released");
        movie.addProperty("tagline", faker.lorem().sentence());
        movie.addProperty("title", faker.book().title());
        movie.addProperty("video", false);
        movie.addProperty("vote_average", random.nextDouble() * 10);
        movie.addProperty("vote_count", random.nextInt(5000));

        return gson.toJson(movie);
    }
}
