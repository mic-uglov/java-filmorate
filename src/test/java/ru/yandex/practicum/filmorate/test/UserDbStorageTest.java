package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserDbStorageTest extends UserStorageTest {
    private static UserStorage storage;

    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorageTest(JdbcTemplate jdbcTemplate) {
        if (storage == null) {
            storage = new UserDbStorage(jdbcTemplate);
        }

        if (UserDbStorageTest.jdbcTemplate == null) {
            UserDbStorageTest.jdbcTemplate = jdbcTemplate;
        }
    }

    @Override
    protected UserStorage getStorage() {
        return storage;
    }

    @BeforeEach
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM friends");
        jdbcTemplate.update("DELETE FROM users");
    }
}
