package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {
    private static final Pattern EMAIL_REGEXP = Pattern.compile(
            "^[A-Z\\d._%+-]+@[A-Z\\d.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern LOGIN_REGEXP = Pattern.compile("^\\S+$");

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    public boolean validate(User user) {
        boolean valid = true;

        if (user.getEmail() == null || !EMAIL_REGEXP.matcher(user.getEmail()).find()) {
            getLogger().warn("Некорректный email: {}", user.getEmail());
            valid = false;
        }
        if (user.getLogin() == null || !LOGIN_REGEXP.matcher(user.getLogin()).find()) {
            getLogger().warn("Некорректный логин: {}", user.getLogin());
            valid = false;
        } else {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
                getLogger().info("Установлено имя пользователя: {}", user.getName());
            }
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            getLogger().warn("Некорректная дата рождения: {}",
                    user.getBirthday().format(DateTimeFormatter.ISO_DATE));
            valid = false;
        }

        return valid;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}