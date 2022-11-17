package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage extends InMemoryAbstractStorage<Film> implements FilmStorage {
    private final Map<Integer, Set<Integer>> likes;
    private final TreeSet<Integer> rating;

    public InMemoryFilmStorage() {
        likes = new HashMap<>();
        rating = new TreeSet<>(this::compareByLikes);
    }

    private int compareByLikes(Integer id1, Integer id2) {
        if (Objects.equals(id1, id2)) {
            return 0;
        }

        int cnt1 = getLikesNumber(id1);
        if (cnt1 == 0) {
            return 1;
        }

        int cnt2 = getLikesNumber(id2);
        if (cnt2 == 0) {
            return -1;
        }

        if (cnt1 == cnt2) {
            return id1 - id2;
        }

        return cnt2 - cnt1;
    }

    private int getLikesNumber(int id) {
        int cnt = 0;

        if (likes.containsKey(id)) {
            Set<Integer> ids = likes.get(id);
            if (ids != null) {
                cnt = ids.size();
            }
        }

        return cnt;
    }

    @Override
    public void create(Film film) {
        super.create(film);
        rating.add(film.getId());
    }

    public void putALike(int filmId, int userId) {
        rating.remove(filmId);

        Set<Integer> filmLikes = likes.computeIfAbsent(filmId, id -> new HashSet<>());

        filmLikes.add(userId);
        rating.add(filmId);
    }

    public void removeALike(int filmId, int userId) {
        Set<Integer> filmLikes = likes.get(filmId);

        if (filmLikes != null && filmLikes.contains(userId)) {
            rating.remove(filmId);
            filmLikes.remove(userId);
            if (filmLikes.size() == 0) {
                likes.remove(filmId);
            }
            rating.add(filmId);
        }
    }

    @Override
    public List<Film> getMostPopular(int count) {
        return rating.stream()
                .limit(count)
                .map(this::get)
                .collect(Collectors.toUnmodifiableList());
    }
}