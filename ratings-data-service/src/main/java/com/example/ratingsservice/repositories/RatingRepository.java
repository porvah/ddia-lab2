package com.example.ratingsservice.repositories;

import com.example.ratingsservice.models.RatingEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RatingRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL = "SELECT * FROM ratings";
    private static final String SELECT_BY_USER_ID = "SELECT * FROM ratings WHERE user_id = ?";
    private static final String INSERT = "INSERT INTO ratings (user_id, movie_id, rating) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM ratings WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM ratings WHERE id = ?";

    public RatingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<RatingEntry> rowMapper = (rs, rowNum) -> {
        RatingEntry entry = new RatingEntry();
        entry.setId(rs.getLong("id"));
        entry.setUserId(rs.getString("user_id"));
        entry.setMovieId(rs.getString("movie_id"));
        entry.setRating(rs.getInt("rating"));
        return entry;
    };

    public List<RatingEntry> findAll() {
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }

    public List<RatingEntry> findByUserId(String userId) {
        return jdbcTemplate.query(SELECT_BY_USER_ID, rowMapper, userId);
    }

    public void save(RatingEntry entry) {
        jdbcTemplate.update(INSERT, entry.getUserId(), entry.getMovieId(), entry.getRating());
    }

    public RatingEntry findById(Long id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID, rowMapper, id);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
