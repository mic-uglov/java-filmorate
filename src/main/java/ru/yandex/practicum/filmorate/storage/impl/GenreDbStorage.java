package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class GenreDbStorage implements GenreStorage {
    private static final String SQL_GET_ALL = "SELECT * FROM genres ORDER BY id";
    private static final String SQL_GET = "SELECT * FROM genres WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, GenreDbStorage::mapRow);
    }

    @Override
    public Optional<Genre> get(int id) {
        List<Genre> genres = jdbcTemplate.query(SQL_GET, GenreDbStorage::mapRow, id);
        if (genres.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(genres.get(0));
        }
    }

    private static Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }
}