package ru.yandex.practicum.filmorate.test.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
public class MpaStorageTest {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaStorageTest(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }
    @Test
    public void testGettingAll() {
        List<MpaRatingItem> mpas = new ArrayList<>(mpaStorage.getAll());

        assertEquals(5, mpas.size());
        assertEquals(1, mpas.get(0).getId());
        assertEquals(5, mpas.get(4).getId());
    }

    @Test
    public void testGettingById() {
        assertEquals("PG-13", mpaStorage.get(3).orElseThrow().getName());
    }

    @Test
    public void testGettingByWrongId() {
        assertTrue(mpaStorage.get(999).isEmpty());
    }
}