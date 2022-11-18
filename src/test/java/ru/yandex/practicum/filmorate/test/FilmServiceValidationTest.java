package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FilmServiceValidationTest {
    private static  ExecutableValidator executableValidator;

    private static FilmService getService() {
        return mock(FilmService.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    }

    @BeforeAll
    public static void setUp() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            executableValidator = factory.getValidator().forExecutables();
        }
    }

    @Test
    public void testGettingMostPopularWithValidCount() throws NoSuchMethodException {
        FilmService service = getService();
        Method getMostPopularMethod = service.getClass().getMethod("getMostPopular", int.class);

        assertTrue(executableValidator.validateParameters(
                service, getMostPopularMethod, new Object[]{100}).isEmpty());

        assertTrue(executableValidator.validateParameters(
                service, getMostPopularMethod, new Object[]{0}).isEmpty());
    }

    @Test
    public void testGettingMostPopularWithNegativeCount() throws NoSuchMethodException {
        FilmService service = getService();
        Method getMostPopularMethod = service.getClass().getMethod("getMostPopular", int.class);

        assertEquals(1,
                executableValidator
                        .validateParameters(service, getMostPopularMethod, new Object[]{-1}).size());
    }
}