package ru.yandex.practicum.filmorate.storage;

public interface FriendStorage {
    boolean addFriend(int userId, int friendId);
    boolean deleteFriend(int userId, int friendId);
}