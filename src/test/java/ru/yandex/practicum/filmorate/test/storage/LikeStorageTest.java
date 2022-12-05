package ru.yandex.practicum.filmorate.test.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

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

    private User getDummyUser(String login) {
        User user = new User();

        user.setLogin(login);
        user.setEmail(login + "@test.tst");
        user.setBirthday(LocalDate.now());

        return user;
    }

    private Film getDummyFilm(String name) {
        Film film = new Film();

        film.setName(name);
        film.setDescription(name);
        film.setDuration(100);
        film.setReleaseDate(LocalDate.now());
        film.setMpa(new MpaRatingItem(1, "test"));

        return film;
    }

    @AfterEach
    public void clear() {
        jdbcTemplate.update("DELETE FROM likes");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM films");
    }

    @Test
    public void testPuttingALike() {
        User user = getDummyUser("login");
        Film film = getDummyFilm("name");

        userStorage.create(user);
        filmStorage.create(film);

        assertTrue(likeStorage.putALike(user.getId(), film.getId()));
    }

    @Test
    public void testPuttingALikeWhenAlreadyExists() {
        User user = getDummyUser("login");
        Film film = getDummyFilm("name");

        userStorage.create(user);
        filmStorage.create(film);
        likeStorage.putALike(user.getId(), film.getId());

        assertFalse(likeStorage.putALike(user.getId(), film.getId()));
    }

    @Test
    public void testPuttingALikeWhenUserDoesNotExist() {
        Film film = getDummyFilm("name");

        filmStorage.create(film);

        assertFalse(likeStorage.putALike(100, film.getId()));
    }

    @Test
    public void testPuttingALikeWhenFilmDoesNotExist() {
        User user = getDummyUser("login");

        userStorage.create(user);

        assertFalse(likeStorage.putALike(user.getId(), 100));
    }

    @Test
    public void testRemovingALike() {
        User user = getDummyUser("user");
        Film film = getDummyFilm("film");

        userStorage.create(user);
        filmStorage.create(film);
        likeStorage.putALike(user.getId(), film.getId());

        assertTrue(likeStorage.removeALike(user.getId(), film.getId()));
    }

    @Test
    public void testRemovingALikeWhenDoesNotExist() {
        assertFalse(likeStorage.removeALike(100, 100));
    }
}