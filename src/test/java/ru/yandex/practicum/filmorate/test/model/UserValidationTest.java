package ru.yandex.practicum.filmorate.test.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.test.util.TestHelper;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidUser() {
        User user = TestHelper.getDummyUser();

        assertTrue(validator.validate(user).isEmpty());
    }

    @Test
    public void testValidationWhenEmailIsInvalid() {
        User user = TestHelper.getDummyUser();

        user.setEmail(null);
        assertEquals(1, validator.validate(user).size());

        user.setEmail("");
        assertEquals(1, validator.validate(user).size());

        user.setEmail("incorrect email");
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    public void testValidationWhenLoginIsInvalid() {
        User user = TestHelper.getDummyUser();

        user.setLogin(null);
        assertEquals(1, validator.validate(user).size());

        user.setLogin("");
        assertEquals(1, validator.validate(user).size());

        user.setLogin("incorrect login");
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    public void testValidationWhenBirthdayIsNull() {
        User user = TestHelper.getDummyUser();

        user.setBirthday(null);
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    public void testValidationWhenBirthdayInvalid() {
        User user = TestHelper.getDummyUser();

        user.setBirthday(LocalDate.now().plusYears(1));
        assertEquals(1, validator.validate(user).size());
    }
}