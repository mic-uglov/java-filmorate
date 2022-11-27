package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Genre extends Item {
    private final String name;

    public Genre(int id, String name) {
        this.name = name;
        this.setId(id);
    }
}