package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Item {
    private static final String USER = "Пользователь";
    private static final String LOGIN_REGEXP = "^\\S+$";

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = LOGIN_REGEXP)
    private String login;

    private String name;

    @NotNull
    @PastOrPresent
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