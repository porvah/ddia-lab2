package com.example.ratingsservice.models;

public class MovieDetails {
        private String movieId;
        private String name;
        private String description;

        public MovieDetails() {}

        public MovieDetails(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }

}
