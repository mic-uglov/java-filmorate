package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.After;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(using = FilmDeserializer.class)
public class Film extends Item {
    private static final String FILM = "Фильм";

    private static final int DESCRIPTION_MAX_LEN = 200;
    private static final String RELEASE_MIN_DATE = "1895-12-28";

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = DESCRIPTION_MAX_LEN)
    private String description;

    @After(RELEASE_MIN_DATE)
    private LocalDate releaseDate;

    @Positive
    private int duration;

    @NotNull
    private MpaRatingItem mpa;

    private Set<Genre> genres;

    public Film() {
        this.genres = new HashSet<>();
    }

    @Override
    public String getItemTypeName() {
        return FILM;
    }

    @Override
    public String getShort() {
        return name;
    }

    public void setGenres(List<Genre> genres) {
        this.genres.clear();
        this.genres.addAll(genres);
    }

    public List<Genre> getGenres() {
        return genres.stream()
                .sorted(Comparator.comparingInt(Item::getId)).collect(Collectors.toUnmodifiableList());
    }
}