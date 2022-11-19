package ru.yandex.practicum.filmorate.test;

import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

public class InMemoryFilmStorageTest extends FilmStorageTest {
    @Override
    protected FilmStorage getStorage() {
        return new InMemoryFilmStorage();
    }
}