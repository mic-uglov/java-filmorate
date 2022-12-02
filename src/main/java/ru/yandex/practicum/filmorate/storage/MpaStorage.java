package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MpaRatingItem;

import java.util.Collection;
import java.util.Optional;

public interface MpaStorage {
    Collection<MpaRatingItem> getAll();
    Optional<MpaRatingItem> get(int id);
}