package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Objects;

@Data
@JsonIgnoreProperties({"itemTypeName", "short"})
public abstract class Item {
    private Integer id;

    public abstract String getItemTypeName();

    public abstract String getShort();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(getId(), item.getId());
    }
}