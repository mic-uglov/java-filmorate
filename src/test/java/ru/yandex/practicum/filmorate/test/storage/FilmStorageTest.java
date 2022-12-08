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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class FilmStorageTest {
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private LikeStorage likeStorage;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void clear() {
        jdbcTemplate.update("DELETE FROM likes");
        jdbcTemplate.update("DELETE FROM films");
        jdbcTemplate.update("DELETE FROM users");
    }

    @Test
    public void testGettingAllWhenEmpty() {
        assertEquals(0, filmStorage.getAll().size());
    }

    @Test
    public void testGettingAll() {
        Film film1 = TestHelper.getDummyFilm("film1");
        Film film2 = TestHelper.getDummyFilm("film2");

        filmStorage.create(film1);
        filmStorage.create(film2);

        List<Film> films = filmStorage.getAll();

        assertEquals(2, films.size());
        assertTrue(films.contains(film1));
        assertTrue(films.contains(film2));
    }

    @Test
    public void testGetting() {
        Film film = TestHelper.getDummyFilm();

        filmStorage.create(film);

        assertEquals(film, filmStorage.get(film.getId()).orElseThrow());
    }

    @Test
    public void testGettingWhenWrongId() {
        assertTrue(filmStorage.get(999).isEmpty());
    }

    @Test
    public void testUpdating() {
        Film film = TestHelper.getDummyFilm();

        filmStorage.create(film);
        film.setName("new name");
        film.setDescription("new description");

        assertTrue(filmStorage.update(film));

        Film updated = filmStorage.get(film.getId()).orElseThrow();

        assertEquals("new name", updated.getName());
        assertEquals("new description", updated.getDescription());
    }

    @Test
    public void testUpdatingWhenDoesNotExist() {
        assertFalse(filmStorage.update(TestHelper.getDummyFilm()));
    }

    @Test
    public void testExistenceChecking() {
        Film film = TestHelper.getDummyFilm();

        filmStorage.create(film);

        assertTrue(filmStorage.exists(film.getId()));
    }

    @Test
    public void testExistenceCheckingWhenDoesNotExist() {
        assertFalse(filmStorage.exists(999));
    }

    @Test
    public void testRatingWhenNoLikes() {
        Film film1 = TestHelper.getDummyFilm("film1");
        Film film2 = TestHelper.getDummyFilm("film2");

        filmStorage.create(film1);
        filmStorage.create(film2);

        assertEquals(List.of(film1, film2), filmStorage.getMostPopular(100));
    }

    @Test
    public void testRatingWhenOneWithLikesAndAnotherWithoutLikes() {
        Film film1 = TestHelper.getDummyFilm("film1");
        Film film2 = TestHelper.getDummyFilm("film2");
        User user = TestHelper.getDummyUser();

        filmStorage.create(film1);
        filmStorage.create(film2);
        userStorage.create(user);
        likeStorage.putALike(user.getId(), film2.getId());

        assertEquals(List.of(film2, film1), filmStorage.getMostPopular(100));
    }

    @Test
    public void testRatingWhenEqualLikesNumber() {
        Film film1 = TestHelper.getDummyFilm("film1");
        Film film2 = TestHelper.getDummyFilm("film2");
        User user = TestHelper.getDummyUser();

        filmStorage.create(film1);
        filmStorage.create(film2);
        userStorage.create(user);
        likeStorage.putALike(user.getId(), film2.getId());
        likeStorage.putALike(user.getId(), film2.getId());

        assertEquals(List.of(film1, film2), filmStorage.getMostPopular(100));
    }
}