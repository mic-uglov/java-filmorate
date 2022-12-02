package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class GenreDbStorage implements GenreStorage {
    private static final String SQL_GET_ALL = "SELECT * FROM genres ORDER BY id";

    private final JdbcTemplate jdbcTemplate;
    private final Map<Integer, Genre> genres;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.genres = new TreeMap<>();
        refresh();
    }

    @Override
    public Collection<Genre> getAll() {
        refresh();
        return genres.values();
    }

    @Override
    public Optional<Genre> get(int id) {
        return Optional.ofNullable(genres.get(id));
    }

    private static Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }

    private void refresh() {
        genres.clear();
        List<Genre> filmGenres = jdbcTemplate.query(SQL_GET_ALL, GenreDbStorage::mapRow);
        filmGenres.forEach(g -> genres.put(g.getId(), g));
    }
}