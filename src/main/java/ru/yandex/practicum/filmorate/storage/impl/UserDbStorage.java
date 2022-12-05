package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDbStorage implements UserStorage {
    public static final String TABLE_NAME = "users";
    private static final String SQL_GET_ALL = "SELECT * FROM users";
    private static final String SQL_GET = "SELECT * FROM users WHERE id = ?";
    private static final String SQL_UPDATE =
            "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String SQL_GET_FRIENDS =
            "SELECT * FROM users u JOIN friends f ON f.friend_id = u.id WHERE f.user_id = ?";
    private static final String SQL_GET_COMMON_FRIENDS=
            "SELECT * FROM users " +
                    "WHERE " +
                        "id IN (SELECT friend_id FROM friends WHERE user_id = ?) AND " +
                        "id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String SQL_EXISTS = "SELECT NULL FROM users WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, UserDbStorage::mapRow);
    }

    @Override
    public void create(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        user.setId(jdbcInsert.executeAndReturnKey(userToMap(user)).intValue());
    }

    @Override
    public Optional<User> get(int id) {
        List<User> users = jdbcTemplate.query(SQL_GET, UserDbStorage::mapRow, id);
        if (users.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(users.get(0));
        }
    }

    @Override
    public boolean exists(int id) {
        return jdbcTemplate.queryForRowSet(SQL_EXISTS, id).next();
    }

    @Override
    public boolean update(User user) {
        int cnt = jdbcTemplate.update(SQL_UPDATE,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId());

        return cnt == 1;
    }

    @Override
    public List<User> getFriends(int userId) {
        return jdbcTemplate.query(SQL_GET_FRIENDS, UserDbStorage::mapRow, userId);
    }

    @Override
    public List<User> getCommonFriends(int id1, int id2) {
        return jdbcTemplate.query(SQL_GET_COMMON_FRIENDS, UserDbStorage::mapRow, id1, id2);
    }

    private static User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());

        return user;
    }

    private static Map<String, Object> userToMap(User user) {
        Map<String, Object> map = new HashMap<>();

        map.put("email", user.getEmail());
        map.put("login", user.getLogin());
        map.put("name", user.getName());
        map.put("birthday", user.getBirthday());

        return map;
    }
}