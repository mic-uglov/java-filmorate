package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"itemTypeName", "short", "description"})
public class MpaRatingItem extends Item {
    private static final String MPA = "MPA";

    private String name;
    private String description;

    public MpaRatingItem() {}

    public MpaRatingItem(int id, String name, String description) {
        this.name = name;
        this.description = description;
        this.setId(id);
    }

    public MpaRatingItem(int id, String name) {
        this(id, name, null);
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