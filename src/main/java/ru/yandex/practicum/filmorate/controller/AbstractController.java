package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.helpers.NOPLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public abstract class AbstractController<T extends Item> {
    private final Storage<T> storage;
    private ObjectMapper objectMapper;

    public AbstractController(Storage<T> storage) {
        this.storage = storage;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<T> getAll() {
        return storage.getAll();
    }

    @PostMapping
    public T create(@RequestBody T item, HttpServletResponse response) {
        getLogger().info("{} {} - создание", item.getItemTypeName(), item.getShort());
        getLogger().trace("{}: {}", item.getClass(), getJsonForTrace(item));

        storage.create(item);

        getLogger().info("{} {} id={} успешно создан", item.getItemTypeName(), item.getShort(), item.getId());

        return item;
    }

    private String getJsonForTrace(T item) {
        if (objectMapper == null || !getLogger().isTraceEnabled()) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @PutMapping
    public T update(@RequestBody T item, HttpServletResponse response) {
        final Integer id = item.getId();

        getLogger().info("{} {} id={} - обновление", item.getItemTypeName(), item.getShort(), id);
        getLogger().trace("{}: {}", item.getClass(), getJsonForTrace(item));

        storage.update(item);

        getLogger().info("{} {} id={} успешно обновлен", item.getItemTypeName(), item.getShort(), id);

        return item;
    }

    protected Logger getLogger() {
        return NOPLogger.NOP_LOGGER;
    }
}