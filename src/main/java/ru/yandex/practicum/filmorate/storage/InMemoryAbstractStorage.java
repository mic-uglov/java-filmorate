package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InMemoryAbstractStorage<T extends Item> implements Storage<T> {
    private int nextId;
    private final Map<Integer, T> items;

    public InMemoryAbstractStorage() {
        nextId = 1;
        items = new HashMap<>();
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public void create(T item) {
        item.setId(nextId++);
        items.put(item.getId(), item);
    }

    @Override
    public void update(T item) {
        items.put(item.getId(), item);
    }

    @Override
    public T get(int id) {
        return items.get(id);
    }

    @Override
    public boolean exists(int id) {
        return items.containsKey(id);
    }
}
