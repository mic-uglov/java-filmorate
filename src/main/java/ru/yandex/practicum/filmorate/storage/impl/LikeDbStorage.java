package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Repository
public class LikeDbStorage implements LikeStorage {
    private static final String SQL_PUT =
            "INSERT INTO likes SELECT ?, ? " +
                    "WHERE NOT EXISTS (" +
                    "SELECT NULL FROM likes WHERE film_id = ? AND user_id = ?)";
    private static final String SQL_REMOVE = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void putALike(int userId, int filmId) {
        jdbcTemplate.update(SQL_PUT, userId, filmId, userId, filmId);
    }

    @Override
    public void removeALike(int userId, int filmId) {
        jdbcTemplate.update(SQL_REMOVE, userId, filmId);
    }
}