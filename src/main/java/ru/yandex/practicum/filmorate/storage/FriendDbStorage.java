package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FriendDbStorage implements FriendStorage {
    private static final String SQL_ADD =
            "INSERT INTO friends SELECT ?, ? " +
                "WHERE NOT EXISTS (" +
                "SELECT NULL FROM friends WHERE user_id = ? AND friend_id = ?) AND " +
                "? IN (SELECT id FROM users) AND " +
                "? IN (SELECT id FROM users)";
    private static final String SQL_DEL = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        jdbcTemplate.update(SQL_ADD, userId, friendId, userId, friendId, userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        jdbcTemplate.update(SQL_DEL, userId, friendId);
    }
}