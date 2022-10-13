package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties({"itemTypeName", "short"})
public abstract class Item {
    private Integer id;

    public abstract String getItemTypeName();
    public abstract String getShort();
}
