package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.service.AbstractService;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableValidator;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("rawtypes")
public class AbstractServiceValidationTest {
    private static ExecutableValidator executableValidator;

    private static AbstractService getService() {
        return mock(AbstractService.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    }

    private static TestValidatingItem getItem() {
        return mock(TestValidatingItem.class);
    }

    @BeforeAll
    public static void setUp() {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            executableValidator = factory.getValidator().forExecutables();
        }
    }

    @Test
    public void shouldValidateItemWhenCreating() throws NoSuchMethodException {
        AbstractService service = getService();
        Method createMethod = service.getClass().getMethod("create", Item.class);

        assertEquals(1,
                executableValidator.validateParameters(service, createMethod, new Object[] { getItem() }).size());
    }

    @Test
    public void shouldValidateItemWhenUpdating() throws NoSuchMethodException {
        AbstractService service = getService();
        Method updateMethod = service.getClass().getMethod("update", Item.class);

        assertEquals(1,
                executableValidator.validateParameters(service, updateMethod, new Object[] { getItem() }).size());
    }

    private abstract static class TestValidatingItem extends Item {
        @NotNull
        @SuppressWarnings("unused")
        private String field;
    }
}