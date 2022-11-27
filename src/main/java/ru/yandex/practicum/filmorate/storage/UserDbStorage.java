package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@Primary
public class UserDbStorage extends AbstractDbStorage<User> implements UserStorage {
    public static final String TABLE_NAME = "users";
    private static final Map<String, String> sqls = Map.of(
            "update", "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?",
            "addFriend", "INSERT INTO friends VALUES (?, ?)",
            "deleteFriend", "DELETE FROM friends WHERE user_id = ? AND friend_id = ?",
            "getFriends", "SELECT * FROM users u JOIN friends f ON f.friend_id = u.id WHERE f.user_id = ?",
            "getCommonFriends", "SELECT * FROM users WHERE " +
                    "id IN (SELECT friend_id FROM friends WHERE user_id = ?) AND " +
                    "id IN (SELECT friend_id FROM friends WHERE user_id = ?)"
    );

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
        return Map.of(
                "email", user.getEmail(),
                "login", user.getLogin(),
                "name", user.getName(),
                "birthday", user.getBirthday()
        );
    }

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, TABLE_NAME);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected RowMapper<User> getRowMapper() {
        return UserDbStorage::mapRow;
    }

    @Override
    protected Map<String, Object> itemToMap(User user) {
        return userToMap(user);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(sqls.get("update"),
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
    }

    @Override
    public void addFriend(int userId, int friendId) {
        jdbcTemplate.update(sqls.get("addFriend"), userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        jdbcTemplate.update(sqls.get("deleteFriend"), userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        return jdbcTemplate.query(sqls.get("getFriends"), getRowMapper(), userId);
    }

    @Override
    public List<User> getCommonFriends(int id1, int id2) {
        return jdbcTemplate.query(sqls.get("getCommonFriends"), getRowMapper(), id1, id2);
    }
}
