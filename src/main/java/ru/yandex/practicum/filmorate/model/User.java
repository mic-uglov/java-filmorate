package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Item {
    private static final String USER = "Пользователь";

    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    @Override
    public String getItemTypeName() {
        return USER;
    }

    @Override
    public String getShort() {
        return login;
    }
}