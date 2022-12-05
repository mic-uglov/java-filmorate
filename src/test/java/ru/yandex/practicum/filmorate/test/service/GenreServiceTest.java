package ru.yandex.practicum.filmorate.test.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GenreServiceTest {
    private final GenreStorage genreStorage;
    private final GenreService genreService;

    public GenreServiceTest() {
        genreStorage = mock(GenreStorage.class);
        genreService = new GenreService(genreStorage);
    }

    @Test
    public void testGettingAll() {
        Genre genre1 = new Genre(1, "test1");
        Genre genre2 = new Genre(2, "test2");
        Genre genre3 = new Genre(3, "test3");

        when(genreStorage.getAll()).thenReturn(List.of(genre1, genre2, genre3));

        assertEquals(List.of(genre1, genre2, genre3), genreService.getAll());
    }

    @Test
    public void testGettingById() {
        when(genreStorage.get(1)).thenReturn(Optional.of(new Genre(1, "test")));

        assertEquals(1, genreService.get(1).orElseThrow().getId());
    }

    @Test
    public void testGettingByIdWhenDoesNotExist() {
        when(genreStorage.get(1)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> genreService.get(1));
    }
}
