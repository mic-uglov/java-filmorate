package ru.yandex.practicum.filmorate.test.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.test.util.TestHelper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
public class FriendStorageTest {
    @Autowired
    private FriendStorage friendStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void clear() {
        jdbcTemplate.update("DELETE FROM friends");
        jdbcTemplate.update("DELETE FROM users");
    }

    @Test
    public void testAddingFriend() {
        User user1 = TestHelper.getDummyUser("user1");
        User user2 = TestHelper.getDummyUser("user2");

        userStorage.create(user1);
        userStorage.create(user2);

        assertTrue(friendStorage.addFriend(user1.getId(), user2.getId()));
    }

    @Test
    public void testAddingFriendWhenAlreadyExists() {
        User user1 = TestHelper.getDummyUser("user1");
        User user2 = TestHelper.getDummyUser("user2");

        userStorage.create(user1);
        userStorage.create(user2);
        friendStorage.addFriend(user1.getId(), user2.getId());

        assertFalse(friendStorage.addFriend(user1.getId(), user2.getId()));
    }

    @Test
    public void testAddingFriendWhenUserDoesNotExists() {
        User user = TestHelper.getDummyUser();

        userStorage.create(user);

        assertFalse(friendStorage.addFriend(100, user.getId()));
    }

    @Test
    public void testAddingFriendWhenFriendDoesNotExists() {
        User user = TestHelper.getDummyUser();

        userStorage.create(user);

        assertFalse(friendStorage.addFriend(user.getId(), 100));
    }

    @Test
    public void testDeletingFriend() {
        User user1 = TestHelper.getDummyUser("user1");
        User user2 = TestHelper.getDummyUser("user2");

        userStorage.create(user1);
        userStorage.create(user2);
        friendStorage.addFriend(user1.getId(), user2.getId());

        assertTrue(friendStorage.deleteFriend(user1.getId(), user2.getId()));
    }

    @Test
    public void testDeletingFriendWhenDoesNotExist() {
        assertFalse(friendStorage.deleteFriend(100, 100));
    }
}
