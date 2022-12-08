package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage storage;

    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    public Collection<Genre> getAll() {
        return storage.getAll();
    }

    public Optional<Genre> get(int id) {
        Optional<Genre> genre = storage.get(id);

        if (genre.isEmpty()) {
            log.error("Не найден жанр id={}", id);
            throw new ItemNotFoundException("Не найден жанр id=" + id);
        }

        return genre;
    }
}