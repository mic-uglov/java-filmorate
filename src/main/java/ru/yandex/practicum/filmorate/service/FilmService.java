package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FilmService extends AbstractService<Film> {
    private final static int DEF_POPULAR_COUNT = 10;

    private final FilmStorage storage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage storage, UserService userService) {
        super(storage);
        this.storage = storage;
        this.userService = userService;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    public void putALike(int filmId, int userId) {
        getLogger().info("Пользователю id={} нравится фильм id={}", userId, filmId);

        check(filmId);
        userService.check(userId);
        storage.putALike(filmId, userId);

        getLogger().info("Пользователь id={} добавил лайк фильму id={}", userId, filmId);
    }

    public void removeALike(int filmId, int userId) {
        getLogger().info("Пользователю id={} не нравится фильм id={}", userId, filmId);

        check(filmId);
        userService.check(userId);
        storage.removeALike(filmId, userId);

        getLogger().info("Пользователь id={} удалил лайк у фильма id={}", userId, filmId);
    }

    public List<Film> getMostPopular(@PositiveOrZero int count) {
        return storage.getMostPopular(count == 0 ? DEF_POPULAR_COUNT : count);
    }

    public Optional<MpaRatingItem> getMpa(int id) {
        Optional<MpaRatingItem> mpa = storage.getMpa(id);

        if (mpa.isEmpty()) {
            getLogger().error("Не найден mpa-рейтинг id={}", id);
            throw new ItemNotFoundException("Не найден mpa-рейтинг id=" + id);
        }

        return mpa;
    }

    public Collection<MpaRatingItem> getMpas() {
        return storage.getMpas();
    }

    public Optional<Genre> getGenre(int id) {
        Optional<Genre> genre = storage.getGenre(id);

        if (genre.isEmpty()) {
            getLogger().error("Не найден жанр id={}", id);
            throw new ItemNotFoundException("Не найден жанр id=" + id);
        }

        return genre;
    }

    public Collection<Genre> getGenres() {
        return storage.getGenres();
    }
}