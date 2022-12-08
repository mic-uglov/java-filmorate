package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {
    boolean putALike(int filmId, int userId);
    boolean removeALike(int userId, int filmId);
}