package ru.yandex.practicum.filmorate.test.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.test.util.TestHelper;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class LikeStorageTest {
    @Autowired
    private LikeStorage likeStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void clear() {
        jdbcTemplate.update("DELETE FROM likes");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM films");
    }

    @Test
    public void testPuttingALike() {
        User user = TestHelper.getDummyUser("login");
        Film film = TestHelper.getDummyFilm("name");

        userStorage.create(user);
        filmStorage.create(film);

        assertTrue(likeStorage.putALike(film.getId(), user.getId()));
    }

    @Test
    public void testPuttingALikeWhenAlreadyExists() {
        User user = TestHelper.getDummyUser();
        Film film = TestHelper.getDummyFilm();

        userStorage.create(user);
        filmStorage.create(film);
        likeStorage.putALike(film.getId(), user.getId());

        assertFalse(likeStorage.putALike(film.getId(), user.getId()));
    }

    @Test
    public void testPuttingALikeWhenUserDoesNotExist() {
        Film film = TestHelper.getDummyFilm();

        filmStorage.create(film);

        assertFalse(likeStorage.putALike(film.getId(), 100));
    }

    @Test
    public void testPuttingALikeWhenFilmDoesNotExist() {
        User user = TestHelper.getDummyUser();

        userStorage.create(user);

        assertFalse(likeStorage.putALike(100, user.getId()));
    }

    @Test
    public void testRemovingALike() {
        User user = TestHelper.getDummyUser();
        Film film = TestHelper.getDummyFilm();

        userStorage.create(user);
        filmStorage.create(film);
        likeStorage.putALike(film.getId(), user.getId());

        assertTrue(likeStorage.removeALike(film.getId(), user.getId()));
    }

    @Test
    public void testRemovingALikeWhenDoesNotExist() {
        assertFalse(likeStorage.removeALike(100, 100));
    }
}