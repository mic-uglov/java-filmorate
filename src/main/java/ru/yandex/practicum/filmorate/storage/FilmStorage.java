package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends Storage<Film> {
    void putALike(int filmId, int userId);
    void removeALike(int filmId, int userId);
    List<Film> getMostPopular(int count);
}