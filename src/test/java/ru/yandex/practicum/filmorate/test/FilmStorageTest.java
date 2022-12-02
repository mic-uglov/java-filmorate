package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class FilmStorageTest {
    private Film film1;
    private Film film2;
    private User user1;

    protected abstract FilmStorage getStorage();

    protected abstract UserStorage getUserStorage();

    private FilmStorage getDummyStorage() {
        FilmStorage storage = getStorage();
        film1 = getDummyFilm("film1");
        film2 = getDummyFilm("film2");
        user1 = getDummyUser("user1");

        storage.create(film1);
        storage.create(film2);
        getUserStorage().create(user1);

        return storage;
    }

    private Film getDummyFilm(String name) {
        Film film = new Film();

        film.setName(name);
        film.setDescription(name);
        film.setReleaseDate(LocalDate.now());
        film.setDuration(100);

        return film;
    }

    private User getDummyUser(String login) {
        User user = new User();

        user.setEmail(login + "@test.ru");
        user.setLogin(login);
        user.setBirthday(LocalDate.now());

        return user;
    }

    @Test
    public void testRatingWhenNoLikes() {
        FilmStorage storage = getDummyStorage();

        assertEquals(film1.getId(), storage.getMostPopular(100).get(0).getId());
        assertEquals(film2.getId(), storage.getMostPopular(100).get(1).getId());
    }

    @Test
    public void testRatingWhenOneWithLikesAndAnotherWithoutLikes() {
        FilmStorage storage = getDummyStorage();

        //storage.putALike(film2.getId(), user1.getId());

        assertEquals(List.of(film2, film1), storage.getMostPopular(100));
    }

    @Test
    public void testRatingWhenEqualLikesNumber() {
        FilmStorage storage = getDummyStorage();

        // storage.putALike(film1.getId(), user1.getId());
        // storage.putALike(film2.getId(), user1.getId());

        assertEquals(List.of(film1, film2), storage.getMostPopular(100));
    }

    @Test
    public void testRatingAfterRemovingLikes() {
        FilmStorage storage = getDummyStorage();

        User user2 = getDummyUser("user2");
        User user3 = getDummyUser("user3");

        getUserStorage().create(user2);
        getUserStorage().create(user3);

        /*storage.putALike(film1.getId(), user1.getId());
        storage.putALike(film1.getId(), user2.getId());
        storage.putALike(film1.getId(), user3.getId());

        storage.putALike(film2.getId(), user1.getId());
        storage.putALike(film2.getId(), user2.getId());

        assertEquals(List.of(film1, film2), storage.getMostPopular(100));

        storage.removeALike(film1.getId(), user1.getId());
        storage.removeALike(film1.getId(), user2.getId());*/

        assertEquals(List.of(film2, film1), storage.getMostPopular(100));
    }

    /*
    @Test
    public void testGettingMpas() {
        FilmStorage storage = getStorage();
        List<MpaRatingItem> mpas = new ArrayList<>(storage.getMpas());

        assertEquals(5, mpas.size());
        assertEquals(1, mpas.get(0).getId());
        assertEquals(5, mpas.get(4).getId());
    }

    @Test
    public void testGettingMpaById() {
        FilmStorage storage = getStorage();

        assertEquals("G", storage.getMpa(1).orElseThrow().getName());
    }

    @Test
    public void testGettingGenres() {
        FilmStorage storage = getStorage();
        List<Genre> genres = new ArrayList<>(storage.getGenres());

        assertEquals(6, genres.size());
        assertEquals(1, genres.get(0).getId());
        assertEquals(6, genres.get(5).getId());
    }

    @Test
    public void testGettingGenreById() {
        FilmStorage storage = getStorage();

        assertEquals("Мультфильм", storage.getGenre(3).orElseThrow().getName());
    }*/
}