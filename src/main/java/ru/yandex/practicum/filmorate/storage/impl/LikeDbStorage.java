package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Repository
public class LikeDbStorage implements LikeStorage {
    private static final String SQL_PUT =
            "INSERT INTO likes (film_id, user_id) SELECT ?, ? " +
                "WHERE " +
                    "NOT EXISTS (" +
                        "SELECT NULL FROM likes WHERE film_id = ? AND user_id = ?) AND " +
                    "? IN (SELECT id FROM films) AND " +
                    "? IN (SELECT id FROM users)";
    private static final String SQL_REMOVE = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean putALike(int filmId, int userId) {
        int cnt = jdbcTemplate.update(SQL_PUT, filmId, userId, filmId, userId, filmId, userId);

        return cnt == 1;
    }

    @Override
    public boolean removeALike(int filmId, int userId) {
        int cnt = jdbcTemplate.update(SQL_REMOVE, filmId, userId);

        return cnt == 1;
    }
}