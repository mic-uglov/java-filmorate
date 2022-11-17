package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.After;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class Film extends Item {
    private static final String FILM = "Фильм";

    private static final int DESCRIPTION_MAX_LEN = 200;
    private static final String RELEASE_MIN_DATE = "1895-12-28";

    @NotBlank
    private String name;

    @Size(max = DESCRIPTION_MAX_LEN)
    private String description;

    @After(RELEASE_MIN_DATE)
    private LocalDate releaseDate;

    @Positive
    private int duration;

    @Override
    public String getItemTypeName() {
        return FILM;
    }

    @Override
    public String getShort() {
        return name;
    }
}