package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.service.AbstractService;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractServiceTest<T extends Item> {
    protected abstract AbstractService<T> getService();
    protected abstract T getItem();

    @Test
    public void shouldCheckExistenceWhenGetting() {
        AbstractService<T> service = getService();

        assertThrows(ItemNotFoundException.class, () -> service.get(1));
    }

    @Test
    public void shouldCheckExistenceWhenUpdating() {
        AbstractService<T> service = getService();
        T item = getItem();
        item.setId(1);

        assertThrows(ItemNotFoundException.class, () -> service.update(item));
    }
}
