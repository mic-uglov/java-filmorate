package ru.yandex.practicum.filmorate.storage;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserStorage extends InMemoryAbstractStorage<User> implements UserStorage {
    private final Map<Integer, Set<Integer>> friends;

    public InMemoryUserStorage() {
        friends = new HashMap<>();
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (exists(userId) && exists(friendId)) {
            Set<Integer> userFriends = friends.computeIfAbsent(userId, id -> new HashSet<>());

            userFriends.add(friendId);
        }
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
    public List<User> getFriends(int userId) {
        Set<Integer> userFriends = friends.get(userId);

        if (userFriends == null) {
            return Collections.emptyList();
        }

        return mapIdsToUsers(userFriends);
    }

    private List<User> mapIdsToUsers(Collection<Integer> ids) {
        return ids.stream()
                .sorted()
                .map(this::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<User> getCommonFriends(int id1, int id2) {
        Set<Integer> friends1 = friends.get(id1);
        Set<Integer> friends2 = friends.get(id2);

        if (friends1 == null || friends2 == null) {
            return Collections.emptyList();
        }

        return mapIdsToUsers(CollectionUtils.intersection(friends1, friends2));
    }
}