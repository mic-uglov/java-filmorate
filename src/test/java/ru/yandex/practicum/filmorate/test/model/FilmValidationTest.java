package ru.yandex.practicum.filmorate.test.model;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTest {
    private static Validator validator;

    private static Film getDummyFilm() {
        Film film = new Film();

        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(100);
        film.setMpa(new MpaRatingItem(1, "mpa_name"));

        return film;
    }

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidFilm() {
        Film film = getDummyFilm();

        assertTrue(validator.validate(film).isEmpty());
    }

    @Test
    public void testValidationWhenNameBlank() {
        Film film = getDummyFilm();

        film.setName(null);
        assertEquals(1, validator.validate(film).size());

        film.setName("");
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    public void testValidationWhenDurationNotPositive() {
        Film film = getDummyFilm();

        film.setDuration(0);
        assertEquals(1, validator.validate(film).size());

        film.setDuration(-100);
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    public void testValidationWhenDescriptionIsTooLong() {
        Film film = getDummyFilm();

        film.setDescription(Strings.repeat("*", 300));
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    public void testValidationWhenReleaseDateTooOld() {
        Film film = getDummyFilm();

        film.setReleaseDate(LocalDate.EPOCH.minusYears(300));
        assertEquals(1, validator.validate(film).size());
    }
}
