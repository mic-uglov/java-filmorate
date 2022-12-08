package ru.yandex.practicum.filmorate.test.util;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class TestHelper {
    private TestHelper() {}

    public static User getDummyUser(String login) {
        User user = new User();

        user.setLogin(login);
        user.setEmail(login + "@test.tst");
        user.setBirthday(LocalDate.now());

        return user;
    }

    public static User getDummyUser() {
        return getDummyUser("user");
    }

    public static Film getDummyFilm(String name) {
        Film film = new Film();

        film.setName(name);
        film.setDescription(name);
        film.setDuration(100);
        film.setReleaseDate(LocalDate.now());
        film.setMpa(new MpaRatingItem(1, "G"));

        return film;
    }

    public static Film getDummyFilm() {
        return getDummyFilm("name");
    }
}