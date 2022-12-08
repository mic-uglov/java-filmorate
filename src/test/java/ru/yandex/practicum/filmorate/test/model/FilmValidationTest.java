package ru.yandex.practicum.filmorate.test.model;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.test.util.TestHelper;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidFilm() {
        Film film = TestHelper.getDummyFilm();

        assertTrue(validator.validate(film).isEmpty());
    }

    @Test
    public void testValidationWhenNameBlank() {
        Film film = TestHelper.getDummyFilm();

        film.setName(null);
        assertEquals(1, validator.validate(film).size());

        film.setName("");
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    public void testValidationWhenDurationNotPositive() {
        Film film = TestHelper.getDummyFilm();

        film.setDuration(0);
        assertEquals(1, validator.validate(film).size());

        film.setDuration(-100);
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    public void testValidationWhenDescriptionIsTooLong() {
        Film film = TestHelper.getDummyFilm();

        film.setDescription(Strings.repeat("*", 300));
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    public void testValidationWhenReleaseDateTooOld() {
        Film film = TestHelper.getDummyFilm();

        film.setReleaseDate(LocalDate.EPOCH.minusYears(300));
        assertEquals(1, validator.validate(film).size());
    }
}