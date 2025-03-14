CREATE TABLE IF NOT EXISTS ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    movie_id VARCHAR(255) NOT NULL,
    rating INT NOT NULL,
    UNIQUE KEY unique_user_movie (user_id, movie_id)
);
