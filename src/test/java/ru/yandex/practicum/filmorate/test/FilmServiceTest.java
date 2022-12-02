package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest extends AbstractServiceTest<Film> {
    private UserService userService;

    @Override
    protected FilmService getService() {
        /*
        userService = new UserService(new InMemoryUserStorage());
        return new FilmService(new InMemoryFilmStorage(), userService);*/
        return null;
    }

    @Override
    protected Film getItem() {
        return new Film();
    }

    @Test
    public void shouldCheckExistenceWhenPuttingALike() {
        FilmService service = getService();

        assertThrows(ItemNotFoundException.class, () -> service.putALike(1, 1));

        service.create(new Film());

        assertThrows(ItemNotFoundException.class, () -> service.putALike(1, 1));

        userService.create(new User());

        assertDoesNotThrow(() -> service.putALike(1, 1));
    }

    @Test
    public void shouldCheckExistenceWhenRemovingALike() {
        FilmService service = getService();

        assertThrows(ItemNotFoundException.class, () -> service.removeALike(1, 1));

        service.create(new Film());

        assertThrows(ItemNotFoundException.class, () -> service.removeALike(1, 1));

        userService.create(new User());

        assertDoesNotThrow(() -> service.removeALike(1, 1));
    }

    @Test
    public void shouldGetDefaultNumberOfPopularWhenCountIsZero() {
        FilmService service = getService();

        service.create(new Film());

        assertEquals(1, service.getMostPopular(0).size());
    }
}
