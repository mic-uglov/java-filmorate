package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends Storage<User> {
    List<User> getFriends(int userId);
    List<User> getCommonFriends(int id1, int id2);
}