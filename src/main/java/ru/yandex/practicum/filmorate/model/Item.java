package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties({"itemTypeName", "short"})
public class Item {
    private Integer id;

    public String getItemTypeName() {
        return null;
    }

    public String getShort() {
        return null;
    }
}