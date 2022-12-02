package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest extends AbstractServiceTest<User> {
    @Override
    protected UserService getService() {
        return null; //new UserService(new InMemoryUserStorage());
    }

    @Override
    protected User getItem() {
        return new User();
    }

    @Test
    public void shouldAutofillNameWhenCreating() {
        UserService service = getService();
        User user = new User();

        user.setLogin("login");
        service.create(user);

        assertEquals("login", user.getName());
    }

    @Test
    public void shouldAutofillNameWhenUpdating() {
        UserService service = getService();
        User user = new User();

        service.create(user);
        user.setLogin("login");
        service.update(user);

        assertEquals("login", user.getName());
    }

    @Test
    public void testBecomingFriends() {
        UserService service = getService();
        User user1 = new User();
        User user2 = new User();

        service.create(user1);
        service.create(user2);

        assertEquals(1, user1.getId());
        assertEquals(2, user2.getId());

        service.becomeFriends(1, 2);

        assertEquals(List.of(user2), service.getFriends(1));

        service.stopBeingFriends(1, 2);
        service.becomeFriends(2, 1);

        assertEquals(List.of(user1), service.getFriends(2));
    }

    @Test
    public void shouldCheckExistenceWhenBecomingFriends() {
        UserService service = getService();

        assertThrows(ItemNotFoundException.class, () -> service.becomeFriends(1, 2));

        service.create(new User());

        assertThrows(ItemNotFoundException.class, () -> service.becomeFriends(1, 2));
    }

    @Test
    public void shouldDoNothingWhenBecomingFriendsSeveralTimes() {
        UserService service = getService();
        User user1 = new User();
        User user2 = new User();

        service.create(user1);
        service.create(user2);
        service.becomeFriends(1, 2);

        assertDoesNotThrow(() -> service.becomeFriends(1, 2));
        assertDoesNotThrow(() -> service.becomeFriends(2, 1));
        assertEquals(List.of(user1), service.getFriends(2));
        assertEquals(List.of(user2), service.getFriends(1));
    }

    @Test
    public void shouldDoNothingWhenBecomingFriendsWithThemself() {
        UserService service = getService();

        service.create(new User());
        service.create(new User());
        service.create(new User());

        assertDoesNotThrow(() -> service.becomeFriends(1, 1));
        assertEquals(0, service.getFriends(1).size());
    }

    @Test
    public void testStoppingBeingFriends() {
        UserService service = getService();

        service.create(new User());
        service.create(new User());
        service.becomeFriends(1, 2);
        service.stopBeingFriends(1, 2);

        assertEquals(0, service.getFriends(1).size());
        assertEquals(0, service.getFriends(2).size());

        service.becomeFriends(1, 2);
        service.stopBeingFriends(2, 1);

        assertEquals(0, service.getFriends(1).size());
        assertEquals(0, service.getFriends(2).size());
    }

    @Test
    public void shouldCheckExistenceWhenStoppingBeingFriends() {
        UserService service = getService();

        assertThrows(ItemNotFoundException.class, () -> service.stopBeingFriends(1, 2));

        service.create(new User());

        assertThrows(ItemNotFoundException.class, () -> service.stopBeingFriends(1, 2));
    }

    @Test
    public void shouldDoNothingWhenNotFriendsStopBeingFriends() {
        UserService service = getService();

        service.create(new User());
        service.create(new User());

        assertDoesNotThrow(() -> service.stopBeingFriends(1, 2));
    }

    @Test
    public void shouldDoNothingWhenStoppingBeingFriendsWithThemself() {
        UserService service = getService();

        service.create(new User());

        assertDoesNotThrow(() -> service.stopBeingFriends(1, 1));
    }

    @Test
    public void testGettingCommonFriends() {
        UserService service = getService();
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        User user4 = new User();

        service.create(user1);
        service.create(user2);
        service.create(user3);
        service.create(user4);
        service.becomeFriends(1, 2);
        service.becomeFriends(1, 3);
        service.becomeFriends(1, 4);
        service.becomeFriends(2, 1);
        service.becomeFriends(2, 3);
        service.becomeFriends(2, 4);
        service.becomeFriends(3, 4);
        service.becomeFriends(4, 3);

        assertEquals(List.of(user3, user4), service.getCommonFriends(1, 2));
        assertEquals(List.of(user3, user4), service.getCommonFriends(2, 1));
    }
}