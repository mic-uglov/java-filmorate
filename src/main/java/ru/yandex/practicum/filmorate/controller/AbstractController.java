package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.service.AbstractService;

import java.util.List;
import java.util.Optional;

public abstract class AbstractController<T extends Item> {
    private final AbstractService<T> service;

    public AbstractController(AbstractService<T> service) {
        this.service = service;
    }

    @GetMapping
    public List<T> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Optional<T> get(@PathVariable int id) {
        return service.get(id);
    }

    @PostMapping
    public T create(@RequestBody T item) {
        return service.create(item);
    }

    @PutMapping
    public T update(@RequestBody T item) {
        return service.update(item);
    }
}