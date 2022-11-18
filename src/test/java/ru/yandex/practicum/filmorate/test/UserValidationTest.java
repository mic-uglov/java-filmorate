package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {
    private static Validator validator;

    private static User getDummyUser() {
        User user = new User();

        user.setLogin("login");
        user.setEmail("email@ya.ru");
        user.setBirthday(LocalDate.now().minusYears(20));

        return user;
    }

    @BeforeAll
    public static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidUser() {
        User user = getDummyUser();

        assertTrue(validator.validate(user).isEmpty());
    }

    @Test
    public void testValidationWhenEmailIsInvalid() {
        User user = getDummyUser();

        user.setEmail(null);
        assertEquals(1, validator.validate(user).size());

        user.setEmail("");
        assertEquals(1, validator.validate(user).size());

        user.setEmail("incorrect email");
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    public void testValidationWhenLoginIsInvalid() {
        User user = getDummyUser();

        user.setLogin(null);
        assertEquals(1, validator.validate(user).size());

        user.setLogin("");
        assertEquals(1, validator.validate(user).size());

        user.setLogin("incorrect login");
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    public void testValidationWhenBirthdayIsNull() {
        User user = getDummyUser();

        user.setBirthday(null);
        assertTrue(validator.validate(user).isEmpty());
    }

    @Test
    public void testValidationWhenBirthdayInvalid() {
        User user = getDummyUser();

        user.setBirthday(LocalDate.now());
        assertEquals(1, validator.validate(user).size());

        user.setBirthday(LocalDate.now().plusYears(1));
        assertEquals(1, validator.validate(user).size());
    }
}