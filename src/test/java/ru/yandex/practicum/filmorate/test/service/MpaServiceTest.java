package ru.yandex.practicum.filmorate.test.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MpaServiceTest {
    private final MpaStorage mpaStorage;
    private final MpaService mpaService;

    public MpaServiceTest() {
        mpaStorage = mock(MpaStorage.class);
        mpaService = new MpaService(mpaStorage);
    }

    @Test
    public void testGettingAll() {
        MpaRatingItem mpa1 = new MpaRatingItem(1, "test1");
        MpaRatingItem mpa2 = new MpaRatingItem(2, "test2");
        MpaRatingItem mpa3 = new MpaRatingItem(3, "test3");

        when(mpaStorage.getAll()).thenReturn(List.of(mpa1, mpa2, mpa3));

        assertEquals(List.of(mpa1, mpa2, mpa3), mpaService.getAll());
    }

    @Test
    public void testGettingById() {
        when(mpaStorage.get(1)).thenReturn(Optional.of(new MpaRatingItem(1, "test")));

        assertEquals(1, mpaService.get(1).orElseThrow().getId());
    }

    @Test
    public void testGettingByIdWhenDoesNotExist() {
        when(mpaStorage.get(1)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> mpaService.get(1));
    }
}
