package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Item;

import java.util.List;

public interface Storage<T extends Item> {
    List<T> getAll();
    void create(T item);
    void update(T item);
    T get(int id);
    boolean exists(int id);
}