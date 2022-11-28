package ru.yandex.practicum.filmorate.test;

import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public class InMemoryFilmStorageTest extends FilmStorageTest {
    @Override
    protected FilmStorage getStorage() {
        return new InMemoryFilmStorage();
    }

    @Override
    protected UserStorage getUserStorage() {
        return new InMemoryUserStorage();
    }
}