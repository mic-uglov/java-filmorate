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
        user1 = getDummyUser("user1");
        user2 = getDummyUser("user2");

        storage.create(user1);
        storage.create(user2);

        return storage;
    }

    private User getDummyUser(String login) {
        User user = new User();

        user.setEmail(login + "@test.ru");
        user.setLogin(login);
        user.setBirthday(LocalDate.now());

        return user;
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

        assertEquals(List.of(user2), storage.getFriends(user1.getId()));
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

        assertEquals(List.of(user2), storage.getFriends(user1.getId()));
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

    @Test
    public void testGettingCommonFriends() {
        UserStorage storage = getDummyStorage();
        User user3 = getDummyUser("user3");
        User user4 = getDummyUser("user4");

        storage.create(user3);
        storage.create(user4);
        storage.addFriend(user1.getId(), user3.getId());
        storage.addFriend(user2.getId(), user3.getId());
        storage.addFriend(user1.getId(), user4.getId());
        storage.addFriend(user2.getId(), user4.getId());
        storage.addFriend(user1.getId(), user2.getId());

        assertEquals(List.of(user3, user4), storage.getCommonFriends(user1.getId(), user2.getId()));
    }
}