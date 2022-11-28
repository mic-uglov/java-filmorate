package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class UserStorageTest {
    protected abstract UserStorage getStorage();

    private User user1;
    private User user2;

    private UserStorage getDummyStorage() {
        UserStorage storage = getStorage();
        user1 = new User();
        user2 = new User();

        user1.setEmail("user1@test.ru");
        user1.setLogin("user1");
        user1.setBirthday(LocalDate.now());

        user2.setEmail("user2@test.ru");
        user2.setLogin("user2");
        user2.setBirthday(LocalDate.now());

        storage.create(user1);
        storage.create(user2);

        return storage;
    }

    @Test
    public void testGettingFriendsWhenNoFriends() {
        UserStorage storage = getDummyStorage();

        assertEquals(Collections.emptyList(), storage.getFriends(user1.getId()));
        assertEquals(Collections.emptyList(), storage.getFriends(user2.getId()));
    }

    @Test
    public void testGettingFriendsWhenUserDoesNotExist() {
        UserStorage storage = getStorage();

        assertEquals(Collections.emptyList(), storage.getFriends(100));
    }

    @Test
    public void testAddingFriend() {
        UserStorage storage = getDummyStorage();

        storage.addFriend(user1.getId(), user2.getId());

        assertEquals(List.of(storage.get(user2.getId()).orElseThrow()),
                storage.getFriends(user1.getId()));
    }

    @Test
    public void testAddingFriendsWhenDoNotExist() {
        UserStorage storage = getStorage();

        assertDoesNotThrow(() -> storage.addFriend(10, 20));
    }

    @Test
    public void testAddingFriendsAgain() {
        UserStorage storage = getDummyStorage();

        storage.addFriend(user1.getId(), user2.getId());
        storage.addFriend(user1.getId(), user2.getId());

        assertEquals(List.of(storage.get(user2.getId()).orElseThrow()), storage.getFriends(user1.getId()));
    }

    @Test
    public void testDeletingFriend() {
        UserStorage storage = getDummyStorage();

        storage.addFriend(user1.getId(), user2.getId());
        storage.deleteFriend(user1.getId(), user2.getId());

        assertEquals(Collections.emptyList(), storage.getFriends(user1.getId()));
    }

    @Test
    public void testDeletingFriendWhenUserDoesNotExists() {
        UserStorage storage = getStorage();

        assertDoesNotThrow(() -> storage.deleteFriend(100, 200));
    }
}