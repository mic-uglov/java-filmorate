package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Genre extends Item {
    private static final String GENRE = "Жанр";

    private String name;

    @SuppressWarnings("unused")
    public Genre() {}

    public Genre(int id, String name) {
        this.name = name;
        this.setId(id);
    }

    @Override
    public String getItemTypeName() {
        return GENRE;
    }

    @Override
    public String getShort() {
        return name;
    }
}