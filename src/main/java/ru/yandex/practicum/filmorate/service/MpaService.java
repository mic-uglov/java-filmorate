package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class MpaService {
    private final MpaStorage storage;

    @Autowired
    public MpaService(MpaStorage storage) {
        this.storage = storage;
    }

    public Collection<MpaRatingItem> getAll() {
        return storage.getAll();
    }

    public Optional<MpaRatingItem> get(int id) {
        Optional<MpaRatingItem> mpa = storage.get(id);

        if (mpa.isEmpty()) {
            log.error("Не найден mpa-рейтинг id={}", id);
            throw new ItemNotFoundException("Не найден mpa-рейтинг id=" + id);
        }

        return mpa;
    }
}