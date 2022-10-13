package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    public static UserController controller = new UserController();

    public static User getDummyUser() {
        User user = new User();

        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@ya.ru");
        user.setBirthday(LocalDate.now().minusYears(20));

        return user;
    }

    @Test
    public void testValidation() {
        assertTrue(controller.validate(getDummyUser()));
    }

    @Test
    public void testValidationWhenLoginIsNull() {
        User user = getDummyUser();

        user.setLogin(null);

        assertFalse(controller.validate(user));
    }

    @Test
    public void testValidationWhenLoginIsIncorrect() {
        User user = getDummyUser();

        user.setLogin("name surname");

        assertFalse(controller.validate(user));
    }

    @Test
    public void testValidationWhenEmailIsNull() {
        User user = getDummyUser();

        user.setEmail(null);

        assertFalse(controller.validate(user));
    }

    @Test
    public void testValidationWhenEmailIsIncorrect() {
        User user = getDummyUser();

        user.setEmail("name surname@ya.ru");

        assertFalse(controller.validate(user));
    }

    @Test
    public void testSettingNameWhenBlankDuringValidation() {
        User user = getDummyUser();

        user.setName(null);
        controller.validate(user);
        assertEquals(user.getLogin(), user.getName());

        user.setName("");
        controller.validate(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void testValidationWhenBirthdayIsIncorrect() {
        User user = getDummyUser();

        user.setBirthday(LocalDate.now().plusDays(1));

        assertFalse(controller.validate(user));
    }
}