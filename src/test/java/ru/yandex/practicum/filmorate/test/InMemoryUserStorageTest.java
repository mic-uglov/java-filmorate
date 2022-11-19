package ru.yandex.practicum.filmorate.test;

import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public class InMemoryUserStorageTest extends UserStorageTest {
    @Override
    protected UserStorage getStorage() {
        return new InMemoryUserStorage();
    }
}