package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {
    void putALike(int userId, int filmId);
    void removeALike(int userId, int filmId);
}
