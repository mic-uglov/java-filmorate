package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage extends Storage<Film> {
    void putALike(int filmId, int userId);
    void removeALike(int filmId, int userId);
    List<Film> getMostPopular(int count);
    Optional<MpaRatingItem> getMpa(int id);
    Collection<MpaRatingItem> getMpas();
    Optional<Genre> getGenre(int id);
    Collection<Genre> getGenres();
}
