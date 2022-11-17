package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService extends AbstractService<User> {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        super(storage);
        this.storage = storage;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    @Override
    protected void autoFill(User user) {
        String name = user.getName();

        if (name != null && !name.isBlank()) {
            return;
        }

        user.setName(user.getLogin());

        getLogger().info("Пользователю id={} установлено имя {}", user.getId(), user.getName());
    }

    public void becomeFriends(int id1, int id2) {
        getLogger().info("Пользователи id={} и id={} хотят стать друзьями", id1, id2);

        check(id1);
        if (id1 != id2) {
            check(id2);
            storage.addFriend(id1, id2);
            storage.addFriend(id2, id1);

            getLogger().info("Пользователи id={} и id={} стали друзьями", id1, id2);
        } else {
            getLogger().info("Пользователь id={} сам себе лучший друг", id1);
        }
    }

    public void stopBeingFriends(int id1, int id2) {
        getLogger().info("Пользователи id={} и id={} не хотят быть друзьями", id1, id2);

        check(id1);
        if (id1 != id2) {
            check(id2);
            storage.deleteFriend(id1, id2);
            storage.deleteFriend(id2, id1);

            getLogger().info("Пользователи id={} и id={} перестали быть друзьями", id1, id2);
        } else {
            getLogger().info("Пользователь id={} сам себе лучший друг", id1);
        }
    }

    public List<User> getCommonFriends(int id1, int id2) {
        check(id1);
        check(id2);
        return CollectionUtils.intersection(storage.getFriends(id1), storage.getFriends(id2)).stream()
                .map(storage::get)
                .sorted(Comparator.comparingInt(Item::getId))
                .collect(Collectors.toUnmodifiableList());
    }

    public List<User> getFriends(int id) {
        check(id);
        return storage.getFriends(id).stream()
                .map(storage::get)
                .sorted(Comparator.comparingInt(Item::getId))
                .collect(Collectors.toUnmodifiableList());
    }
}