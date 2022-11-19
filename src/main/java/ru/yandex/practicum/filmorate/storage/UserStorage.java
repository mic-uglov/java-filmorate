package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends Storage<User> {
    void addFriend(int userId, int friendId);
    void deleteFriend(int userId, int friendId);
    List<Integer> getFriends(int userId);
}