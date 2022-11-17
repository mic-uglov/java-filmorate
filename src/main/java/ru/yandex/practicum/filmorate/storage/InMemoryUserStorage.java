package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage extends InMemoryAbstractStorage<User> implements UserStorage {
    private final Map<Integer, Set<Integer>> friends;

    public InMemoryUserStorage() {
        friends = new HashMap<>();
    }

    @Override
    public void addFriend(int userId, int friendId) {
        Set<Integer> userFriends = friends.computeIfAbsent(userId, id -> new HashSet<>());

        userFriends.add(friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        Set<Integer> userFriends = friends.get(userId);

        if (userFriends != null) {
            userFriends.remove(friendId);
            if (userFriends.isEmpty()) {
                friends.remove(userId);
            }
        }
    }

    @Override
    public List<Integer> getFriends(int userId) {
        return new ArrayList<>(friends.getOrDefault(userId, Collections.emptySet()));
    }
}