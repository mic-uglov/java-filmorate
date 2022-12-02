package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {
    boolean putALike(int userId, int filmId);
    boolean removeALike(int userId, int filmId);
}
