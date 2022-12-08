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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserStorageTest {
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private FriendStorage friendStorage;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void clear() {
        jdbcTemplate.update("DELETE FROM friends");
        jdbcTemplate.update("DELETE FROM users");
    }

    @Test
    public void testGettingAllWhenEmpty() {
        assertEquals(0, userStorage.getAll().size());
    }

    @Test
    public void testGettingAll() {
        User user1 = TestHelper.getDummyUser("user1");
        User user2 = TestHelper.getDummyUser("user2");

        userStorage.create(user1);
        userStorage.create(user2);

        List<User> users = userStorage.getAll();

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    public void testGetting() {
        User user = TestHelper.getDummyUser();

        userStorage.create(user);

        assertEquals(user, userStorage.get(user.getId()).orElseThrow());
    }

    @Test
    public void testGettingWhenWrongId() {
        assertTrue(userStorage.get(999).isEmpty());
    }

    @Test
    public void testUpdating() {
        User user = TestHelper.getDummyUser();

        userStorage.create(user);
        user.setName("new name");
        user.setEmail("new_email@test.tst");

        assertTrue(userStorage.update(user));

        User updated = userStorage.get(user.getId()).orElseThrow();

        assertEquals("new name", updated.getName());
        assertEquals("new_email@test.tst", updated.getEmail());
    }

    @Test
    public void testUpdatingWhenDoesNotExist() {
        assertFalse(userStorage.update(TestHelper.getDummyUser()));
    }

    @Test
    public void testExistenceChecking() {
        User user = TestHelper.getDummyUser();

        userStorage.create(user);

        assertTrue(userStorage.exists(user.getId()));
    }

    @Test
    public void testExistenceCheckingWhenDoesNotExist() {
        assertFalse(userStorage.exists(999));
    }

    @Test
    public void testGettingFriendsWhenNoFriends() {
        User user1 = TestHelper.getDummyUser("user1");
        User user2 = TestHelper.getDummyUser("user2");

        userStorage.create(user1);
        userStorage.create(user2);

        assertEquals(Collections.emptyList(), userStorage.getFriends(user1.getId()));
        assertEquals(Collections.emptyList(), userStorage.getFriends(user2.getId()));
    }

    @Test
    public void testGettingFriendsWhenUserDoesNotExist() {
        assertEquals(Collections.emptyList(), userStorage.getFriends(100));
    }

    @Test
    public void testGettingCommonFriends() {
        User user1 = TestHelper.getDummyUser("user1");
        User user2 = TestHelper.getDummyUser("user2");
        User user3 = TestHelper.getDummyUser("user3");
        User user4 = TestHelper.getDummyUser("user4");

        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        userStorage.create(user4);
        friendStorage.addFriend(user1.getId(), user3.getId());
        friendStorage.addFriend(user2.getId(), user3.getId());
        friendStorage.addFriend(user1.getId(), user4.getId());
        friendStorage.addFriend(user2.getId(), user4.getId());
        friendStorage.addFriend(user1.getId(), user2.getId());

        assertEquals(List.of(user3, user4), userStorage.getCommonFriends(user1.getId(), user2.getId()));
    }

    @Test
    public void testGettingCommonFriendsWhenNoInCommon() {
        User user1 = TestHelper.getDummyUser("user1");
        User user2 = TestHelper.getDummyUser("user2");
        User user3 = TestHelper.getDummyUser("user3");
        User user4 = TestHelper.getDummyUser("user4");

        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        userStorage.create(user4);
        friendStorage.addFriend(user1.getId(), user3.getId());
        friendStorage.addFriend(user2.getId(), user4.getId());
        friendStorage.addFriend(user1.getId(), user2.getId());
        friendStorage.addFriend(user2.getId(), user1.getId());

        assertEquals(Collections.emptyList(), userStorage.getCommonFriends(user1.getId(), user2.getId()));
    }
}