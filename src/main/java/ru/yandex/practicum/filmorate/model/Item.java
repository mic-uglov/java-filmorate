package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public abstract class Item {
    private Integer id;

    public abstract String getItemTypeName();
    public abstract String getShort();
}
