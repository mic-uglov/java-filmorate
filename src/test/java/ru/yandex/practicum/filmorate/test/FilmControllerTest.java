package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    /*
    public static FilmController controller = new FilmController();

    public static Film getDummyFilm() {
        Film film = new Film();

        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(1);

        return film;
    }

    @Test
    public void testValidation() {
        Film film = getDummyFilm();

        assertTrue(controller.validate(film));
    }

    @Test
    public void testValidationWhenNameIsEmpty() {
        Film film = getDummyFilm();

        film.setName(null);
        assertFalse(controller.validate(film));

        film.setName("");
        assertFalse(controller.validate(film));
    }

    @Test
    public void testValidationWhenDescriptionIsTooLong() {
        Film film = getDummyFilm();

        film.setDescription(" ".repeat(201));

        assertFalse(controller.validate(film));
    }

    @Test
    public void testValidationWhenReleaseDateIsTooOld() {
        Film film = getDummyFilm();
        LocalDate veryOldDate = LocalDate.of(1895, 12, 27);

        film.setReleaseDate(veryOldDate);

        assertFalse(controller.validate(film));
    }

    @Test
    public void testValidationWhenDurationIsNegative() {
        Film film = getDummyFilm();

        film.setDuration(-1);

        assertFalse(controller.validate(film));
    }
     */
}
