package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("rawtypes")
public abstract class AbstractStorageTest {
    private static Item getItem() {
        return new Item();
    }

    protected abstract Storage getStorage();

    @Test
    public void testGetAllItems() {
        Storage storage = getStorage();
        Item item1 = getItem();
        Item item2 = getItem();

        storage.create(item1);
        storage.create(item2);

        assertEquals(List.of(item1, item2), storage.getAll());
    }

    @Test
    public void testGetAllItemsWhenNoItems() {
        Storage storage = getStorage();

        assertEquals(Collections.emptyList(), storage.getAll());
    }

    @Test
    public void testCreation() {
        Storage storage = getStorage();
        Item item = getItem();

        storage.create(item);

        assertEquals(1, item.getId());
    }

    @Test
    public void testGetting() {
        Storage storage = getStorage();
        Item item = getItem();

        storage.create(item);

        assertEquals(item, storage.get(item.getId()).orElseThrow());
    }

    @Test
    public void testGettingWhenNotExists() {
        Storage storage = getStorage();

        assertTrue(storage.get(999).isEmpty());
    }

    @Test
    public void testUpdating() {
        Storage storage = getStorage();
        Item item = getItem();

        storage.create(item);
        storage.update(item);

        assertSame(item, storage.get(item.getId()).orElseThrow());
    }

    @Test
    public void testUpdatingWhenNotExists() {
        Storage storage = getStorage();
        Item item = getItem();

        storage.update(item);

        assertNull(item.getId());
        assertEquals(List.of(item), storage.getAll());
    }

    @Test
    public void testExistsWhenExists() {
        Storage storage = getStorage();
        Item item = getItem();

        storage.create(item);

        assertTrue(storage.exists(item.getId()));
    }

    @Test
    public void testExistsWhenDoesNotExist() {
        Storage storage = getStorage();

        assertFalse(storage.exists(999));
    }
}