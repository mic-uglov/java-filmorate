package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"itemTypeName", "short", "description"})
public class MpaRatingItem extends Item {
    private static final String MPA = "MPA";

    private final String name;
    private final String description;

    public MpaRatingItem(int id, String name, String description) {
        this.name = name;
        this.description = description;
        this.setId(id);
    }

    @Override
    public String getItemTypeName() {
        return MPA;
    }

    @Override
    public String getShort() {
        return name;
    }
}