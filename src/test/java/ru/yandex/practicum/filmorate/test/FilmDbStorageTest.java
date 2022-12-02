package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@SpringBootTest
@AutoConfigureTestDatabase
public class FilmDbStorageTest extends FilmStorageTest {
    private static FilmStorage storage;
    private static UserStorage userStorage;

    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorageTest(JdbcTemplate jdbcTemplate) {
        if (storage == null) {
            //storage = new FilmDbStorage(jdbcTemplate);
        }

        if (userStorage == null) {
            userStorage = new UserDbStorage(jdbcTemplate);
        }

        if (FilmDbStorageTest.jdbcTemplate == null) {
            FilmDbStorageTest.jdbcTemplate = jdbcTemplate;
        }
    }

    @Override
    protected FilmStorage getStorage() {
        return storage;
    }

    @Override
    protected UserStorage getUserStorage() {
        return userStorage;
    }

    @BeforeEach
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM likes");
        jdbcTemplate.update("DELETE FROM film_genre");
        jdbcTemplate.update("DELETE FROM films");
        jdbcTemplate.update("DELETE FROM users");
    }
}