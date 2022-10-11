package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class Film extends Item {
    private static final String FILM = "Фильм";

    private String name;
    private String description;
    private LocalDate releaseDate;
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