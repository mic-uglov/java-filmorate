package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.model.Item;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractController<T extends Item> {
    private int currentId;
    private final Map<Integer, T> items;

    public AbstractController() {
        currentId = 0;
        items = new HashMap<>();
    }

    @GetMapping
    public List<T> getAll() {
        return new ArrayList<>(items.values());
    }

    @PostMapping
    public T create(T item) {
        item.setId(currentId++);
        items.put(item.getId(), item);
        return item;
    }

    @PutMapping
    public T update(T item, HttpServletResponse response) {
        Integer id = item.getId();

        if (id == null || !items.containsKey(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            items.put(id, item);
        }

        return item;
    }
}
