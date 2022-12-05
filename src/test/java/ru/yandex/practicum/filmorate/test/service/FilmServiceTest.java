package ru.yandex.practicum.filmorate.test.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FilmServiceTest {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;
    private final FilmService filmService;

    public FilmServiceTest() {
        filmStorage = mock(FilmStorage.class);
        likeStorage = mock(LikeStorage.class);
        userStorage = mock(UserStorage.class);
        filmService = new FilmService(
                filmStorage,
                likeStorage,
                new UserService(userStorage, mock(FriendStorage.class)));
    }

    @Test
    public void shouldCheckUserExistenceWhenPuttingALike() {
        when(likeStorage.putALike(1, 1)).thenReturn(false);
        when(filmStorage.exists(1)).thenReturn(true);
        when(userStorage.exists(1)).thenReturn(false);

        assertThrows(ItemNotFoundException.class, () -> filmService.putALike(1, 1));
    }

    @Test
    public void shouldCheckFilmExistenceWhenPuttingALike() {
        when(likeStorage.putALike(1, 1)).thenReturn(false);
        when(filmStorage.exists(1)).thenReturn(false);
        when(userStorage.exists(1)).thenReturn(true);

        assertThrows(ItemNotFoundException.class, () -> filmService.putALike(1, 1));
    }

    @Test
    public void shouldCheckUserExistenceWhenRemovingALike() {
        when(likeStorage.putALike(1, 1)).thenReturn(false);
        when(filmStorage.exists(1)).thenReturn(true);
        when(userStorage.exists(1)).thenReturn(false);

        assertThrows(ItemNotFoundException.class, () -> filmService.removeALike(1, 1));
    }

    @Test
    public void shouldCheckFilmExistenceWhenRemovingALike() {
        when(likeStorage.putALike(1, 1)).thenReturn(false);
        when(filmStorage.exists(1)).thenReturn(false);
        when(userStorage.exists(1)).thenReturn(true);

        assertThrows(ItemNotFoundException.class, () -> filmService.removeALike(1, 1));
    }

    @Test
    public void shouldGetDefaultNumberOfPopularWhenCountIsNull() {
        ArgumentCaptor<Integer> countCaptor = ArgumentCaptor.forClass(Integer.class);
        when(filmStorage.getMostPopular(countCaptor.capture())).thenReturn(Collections.emptyList());

        filmService.getMostPopular(null);

        assertEquals(10, countCaptor.getValue());
    }
}