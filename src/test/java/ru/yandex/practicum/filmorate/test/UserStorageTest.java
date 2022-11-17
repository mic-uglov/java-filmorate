package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class UserStorageTest {
    protected abstract UserStorage getStorage();

    private UserStorage getDummyStorage() {
        UserStorage storage = getStorage();

        storage.create(new User());
        storage.create(new User());

        return storage;
    }

    @Test
    public void testGettingFriendsWhenNoFriends() {
        UserStorage storage = getDummyStorage();

        assertEquals(Collections.emptyList(), storage.getFriends(1));
        assertEquals(Collections.emptyList(), storage.getFriends(2));
    }

    @Test
    public void testGettingFriendsWhenUserDoesNotExist() {
        UserStorage storage = getStorage();

        assertEquals(Collections.emptyList(), storage.getFriends(100));
    }

    @Test
    public void testAddingFriend() {
        UserStorage storage = getDummyStorage();

        storage.addFriend(1, 2);

        assertEquals(List.of(2), storage.getFriends(1));
    }

    @Test
    public void testAddingFriendsWhenDoNotExist() {
        UserStorage storage = getStorage();

        storage.addFriend(10, 20);

        assertEquals(List.of(20), storage.getFriends(10));
    }

    @Test
    public void testAddingFriendsAgain() {
        UserStorage storage = getDummyStorage();

        storage.addFriend(1, 2);
        storage.addFriend(1, 2);

        assertEquals(List.of(2), storage.getFriends(1));
    }

    @Test
    public void testDeletingFriend() {
        UserStorage storage = getDummyStorage();

        storage.addFriend(1, 2);
        storage.deleteFriend(1, 2);

        assertEquals(Collections.emptyList(), storage.getFriends(1));
    }

    @Test
    public void testDeletingFriendWhenUserDoesNotExists() {
        UserStorage storage = getStorage();

        assertDoesNotThrow(() -> storage.deleteFriend(100, 200));
    }
}