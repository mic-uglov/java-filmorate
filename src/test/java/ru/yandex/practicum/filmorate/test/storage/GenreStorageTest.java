package ru.yandex.practicum.filmorate.test.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class GenreStorageTest {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreStorageTest(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }
    @Test
    public void testGettingAll() {
        List<Genre> genres = new ArrayList<>(genreStorage.getAll());

        assertEquals(6, genres.size());
        assertEquals(1, genres.get(0).getId());
        assertEquals(6, genres.get(5).getId());
    }

    @Test
    public void testGettingById() {
        assertEquals("Мультфильм", genreStorage.get(3).orElseThrow().getName());
    }

    @Test
    public void testGettingByWrongId() {
        assertTrue(genreStorage.get(999).isEmpty());
    }
}