package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryFilmStorage extends InMemoryAbstractStorage<Film> implements FilmStorage {
    private static final Map<Integer, Genre> genres = new TreeMap<>(Map.of(
            1, new Genre(1, "Комедия"),
            2, new Genre(2, "Драма"),
            3, new Genre(3, "Мультфильм"),
            4, new Genre(4, "Триллер"),
            5, new Genre(5, "Документальный"),
            6, new Genre(6, "Боевик")
    ));
    private static final Map<Integer, MpaRatingItem> mpas = new TreeMap<>(Map.of(
            1, new MpaRatingItem(1, "G", "у фильма нет возрастных ограничений"),
            2, new MpaRatingItem(2, "PG", "детям рекомендуется смотреть фильм с родителями"),
            3, new MpaRatingItem(3, "PG-13", "детям до 13 лет просмотр не желателен"),
            4, new MpaRatingItem(4, "R",
                    "лицам до 17 лет просматривать фильм можно только в присутствии взрослого"),
            5, new MpaRatingItem(5, "NC-17", "лицам до 18 лет просмотр запрещён")
    ));

    private final Map<Integer, Set<Integer>> likes;
    private final Set<Integer> rating;

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
                .map(this::get)
                .filter(Optional::isPresent)
                .limit(count)
                .map(Optional::get)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<MpaRatingItem> getMpa(int id) {
        return Optional.ofNullable(mpas.get(id));
    }

    @Override
    public Collection<MpaRatingItem> getMpas() {
        return mpas.values();
    }

    @Override
    public Optional<Genre> getGenre(int id) {
        return Optional.ofNullable(genres.get(id));
    }

    @Override
    public Collection<Genre> getGenres() {
        return genres.values();
    }
}