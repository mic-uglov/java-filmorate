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

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractController<T extends Item> {
    @Autowired
    private ObjectMapper objectMapper;

    private int nextId;
    private final Map<Integer, T> items;

    public AbstractController() {
        nextId = 1;
        items = new HashMap<>();
    }

    @GetMapping
    public List<T> getAll() {
        return new ArrayList<>(items.values());
    }

    @PostMapping
    public T create(@RequestBody T item, HttpServletResponse response) {
        getLogger().info("{} {} - создание", item.getItemTypeName(), item.getShort());
        getLogger().trace("{}: {}", item.getClass(), getJsonForTrace(item));

        final int id = nextId++;

        if (validate(item)) {
            item.setId(id);
            items.put(id, item);
            getLogger().info("{} {} id={} успешно создан", item.getItemTypeName(), item.getShort(), id);
        } else {
            getLogger().warn("{} {} - переданы некорректные данные для создания",
                    item.getItemTypeName(), item.getShort());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return item;
    }

    private String getJsonForTrace(T item) {
        if (!getLogger().isTraceEnabled()) {
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

        if (id == null || !items.containsKey(id)) {
            getLogger().warn("{} {} id={} не найден", item.getItemTypeName(), item.getShort(), id);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            if (validate(item)) {
                getLogger().info("{} {} id={} успешно обновлен", item.getItemTypeName(), item.getShort(), id);
                items.put(id, item);
            } else {
                getLogger().warn("{} {} id={} - переданы некорректные данные для обновления",
                        item.getItemTypeName(), item.getShort(), id);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

        return item;
    }

    protected Logger getLogger() {
        return NOPLogger.NOP_LOGGER;
    }

    public abstract boolean validate(T item);
}