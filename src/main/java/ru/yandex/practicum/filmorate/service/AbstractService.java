package ru.yandex.practicum.filmorate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.helpers.NOPLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Validated
public abstract class AbstractService<T extends Item> {
    private final Storage<T> storage;
    private ObjectMapper objectMapper;

    public AbstractService(Storage<T> storage) {
        this.storage = storage;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<T> getAll() {
        return storage.getAll();
    }

    public Optional<T> get(int id) {
        getLogger().info("Получение объекта id={}", id);

        check(id);

        Optional<T> optionalItem = storage.get(id);
        T item = optionalItem.orElseThrow();

        getLogger().info("Найден {} {}", item.getItemTypeName(), item.getShort());
        getLogger().trace("{}: {}", item.getClass(), getJsonForTrace(item));

        return optionalItem;
    }

    public T create(@Valid T item) {
        getLogger().info("{} {} - создание", item.getItemTypeName(), item.getShort());
        getLogger().trace("{}: {}", item.getClass(), getJsonForTrace(item));

        autoFill(item);
        storage.create(item);

        getLogger().info("{} {} id={} успешно создан", item.getItemTypeName(), item.getShort(), item.getId());

        return item;
    }

    public T update(@Valid T item) {
        final Integer id = item.getId();

        getLogger().info("{} {} id={} - обновление", item.getItemTypeName(), item.getShort(), id);
        getLogger().trace("{}: {}", item.getClass(), getJsonForTrace(item));

        check(item.getId());
        autoFill(item);
        storage.update(item);

        getLogger().info("{} {} id={} успешно обновлен", item.getItemTypeName(), item.getShort(), id);

        return item;
    }

    protected void check(int id) {
        if (!storage.exists(id)) {
            getLogger().error("Не найден объект id={}", id);
            throw new ItemNotFoundException("Не найден объект id=" + id);
        }
    }

    protected Logger getLogger() {
        return NOPLogger.NOP_LOGGER;
    }

    protected void autoFill(T item) {
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
}