package ru.yandex.practicum.filmorate.test.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.service.AbstractService;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("rawtypes")
public class AbstractServiceTest {
    private final Storage storage;
    private final AbstractService service;

    public AbstractServiceTest() {
        storage = mock(Storage.class);
        service = mock(AbstractService.class,
                withSettings().useConstructor(storage).defaultAnswer(CALLS_REAL_METHODS));
    }

    @Test
    public void testGettingAll() {
        Item item1 = mock(Item.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        Item item2 = mock(Item.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));

        item1.setId(1);
        item2.setId(2);
        when(storage.getAll()).thenReturn(List.of(item1, item2));

        assertEquals(List.of(item1, item2), service.getAll());
    }

    @Test
    public void testGettingById() {
        Item item = mock(Item.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));

        item.setId(1);
        when(storage.get(1)).thenReturn(Optional.of(item));

        assertEquals(1, ((Item) service.get(1).orElseThrow()).getId());
    }

    @Test
    public void testGettingByIdWhenDoesNotExist() {
        when(storage.get(1)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> service.get(1));
    }

    @Test
    public void shouldCheckExistenceWhenGetting() {
        when(storage.get(1)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> service.get(1));
    }

    @Test
    public void shouldCheckExistenceWhenUpdating() {
        Item item = mock(Item.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));

        item.setId(1);
        when(storage.update(item)).thenReturn(false);

        assertThrows(ItemNotFoundException.class, () -> service.update(item));
    }
}