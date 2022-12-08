package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Item;

import java.util.List;
import java.util.Optional;

public interface Storage<T extends Item> {
    List<T> getAll();
    void create(T item);
    boolean update(T item);
    Optional<T> get(int id);
    boolean exists(int id);
}