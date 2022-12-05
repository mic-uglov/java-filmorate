package ru.yandex.practicum.filmorate.test.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;
    private final UserService userService;

    public UserServiceTest() {
        userStorage = mock(UserStorage.class);
        friendStorage = mock(FriendStorage.class);
        userService = new UserService(userStorage, friendStorage);
    }

    @Test
    public void shouldAutofillNameWhenCreating() {
        User user = new User();

        user.setLogin("login");
        userService.create(user);

        assertEquals("login", user.getName());
    }

    @Test
    public void shouldAutofillNameWhenUpdating() {
        User user = new User();

        userService.create(user);
        user.setLogin("login");
        when(userStorage.update(user)).thenReturn(true);
        userService.update(user);

        assertEquals("login", user.getName());
    }

    @Test
    public void shouldCheckExistenceWhenBecomingFriends() {
        when(friendStorage.addFriend(1, 2)).thenReturn(false);
        when(userStorage.exists(1)).thenReturn(true);
        when(userStorage.exists(2)).thenReturn(false);
        assertThrows(ItemNotFoundException.class, () -> userService.becomeFriends(1, 2));

        when(friendStorage.addFriend(1, 2)).thenReturn(false);
        when(userStorage.exists(1)).thenReturn(false);
        when(userStorage.exists(2)).thenReturn(true);
        assertThrows(ItemNotFoundException.class, () -> userService.becomeFriends(1, 2));
    }

    @Test
    public void shouldDoNothingWhenBecomingFriendsWithThemself() {
        assertDoesNotThrow(() -> userService.becomeFriends(1, 1));
    }

    @Test
    public void shouldCheckExistenceWhenStoppingBeingFriends() {
        when(friendStorage.addFriend(1, 2)).thenReturn(false);
        when(userStorage.exists(1)).thenReturn(true);
        when(userStorage.exists(2)).thenReturn(false);
        assertThrows(ItemNotFoundException.class, () -> userService.becomeFriends(1, 2));

        when(friendStorage.addFriend(1, 2)).thenReturn(false);
        when(userStorage.exists(1)).thenReturn(false);
        when(userStorage.exists(2)).thenReturn(true);
        assertThrows(ItemNotFoundException.class, () -> userService.becomeFriends(1, 2));
    }

    @Test
    public void shouldDoNothingWhenNotFriendsStopBeingFriends() {
        when(friendStorage.addFriend(1, 2)).thenReturn(false);
        when(userStorage.exists(1)).thenReturn(true);
        when(userStorage.exists(2)).thenReturn(false);
        assertThrows(ItemNotFoundException.class, () -> userService.stopBeingFriends(1, 2));

        when(friendStorage.addFriend(1, 2)).thenReturn(false);
        when(userStorage.exists(1)).thenReturn(false);
        when(userStorage.exists(2)).thenReturn(true);
        assertThrows(ItemNotFoundException.class, () -> userService.stopBeingFriends(1, 2));
    }

    @Test
    public void shouldDoNothingWhenStoppingBeingFriendsWithThemself() {
        assertDoesNotThrow(() -> userService.stopBeingFriends(1, 1));
    }
}