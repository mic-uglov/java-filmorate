package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorage implements FilmStorage {
    private static final String TABLE_NAME = "films";

    private static final String SQL_GET_ALL =
            "SELECT " +
                    "f.*, " +
                    "m.name mpa_name, " +
                    "LISTAGG(fg.genre_id || ':' || g.name, ',') WITHIN GROUP (ORDER BY fg.genre_id) genres " +
                "FROM films f " +
                    "LEFT JOIN film_genre fg ON fg.film_id = f.id " +
                    "LEFT JOIN genres g ON g.id = fg.genre_id " +
                    "JOIN mpa_rating m ON m.id = f.mpa " +
                "GROUP BY f.id";
    private static final String SQL_GET =
            "SELECT " +
                    "f.*, " +
                    "m.name mpa_name, " +
                    "LISTAGG(fg.genre_id || ':' || g.name, ',') WITHIN GROUP (ORDER BY fg.genre_id) genres " +
                "FROM films f " +
                    "LEFT JOIN film_genre fg ON fg.film_id = f.id " +
                    "LEFT JOIN genres g ON g.id = fg.genre_id " +
                    "JOIN mpa_rating m ON m.id = f.mpa " +
                "WHERE f.id = ? " +
                "GROUP BY f.id";
    private static final String SQL_UPDATE =
            "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa = ? " +
                    "WHERE id = ?";
    private static final String SQL_GET_FILM_GENRES =
            "SELECT genre_id FROM film_genre WHERE film_id = ? ORDER BY genre_id";
    private static final String SQL_DELETE_FILM_GENRES = "DELETE FROM film_genre WHERE film_id = ?";
    private static final String SQL_INSERT_FILM_GENRE = "INSERT INTO film_genre VALUES (?, ?)";
    private static final String SQL_GET_MOST_POPULAR =
            "SELECT f.*, " +
                    "m.name mpa_name, " +
                    "LISTAGG(fg.genre_id || ':' || g.name, ',') WITHIN GROUP (ORDER BY fg.genre_id) genres " +
                "FROM films f " +
                    "LEFT JOIN film_genre fg ON fg.film_id = f.id " +
                    "LEFT JOIN likes l ON l.film_id = f.id " +
                    "LEFT JOIN genres g ON g.id = fg.genre_id " +
                    "JOIN mpa_rating m ON m.id = f.mpa " +
                "GROUP BY f.id " +
                "ORDER BY COUNT(l.user_id) DESC, f.id " +
                "LIMIT ?";
    private static final String SQL_EXISTS = "SELECT NULL FROM films WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, FilmDbStorage::mapRow);
    }

    @Override
    public Optional<Film> get(int id) {
        List<Film> films = jdbcTemplate.query(SQL_GET, FilmDbStorage::mapRow, id);
        if (films.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(films.get(0));
        }
    }

    @Override
    public boolean exists(int id) {
        return jdbcTemplate.queryForRowSet(SQL_EXISTS, id).next();
    }

    @Override
    public void create(Film film) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        film.setId(jdbcInsert.executeAndReturnKey(filmToMap(film)).intValue());
        insertFilmGenres(film);
    }

    @Override
    public boolean update(Film film) {
        int cnt = jdbcTemplate.update(SQL_UPDATE,
                film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(),
                film.getId());

        if (cnt == 0) {
            return false;
        }

        if (!film.getGenres().stream()
                    .map(Item::getId)
                    .collect(Collectors.toUnmodifiableList())
                .equals(getFilmGenres(film))) {
            jdbcTemplate.update(SQL_DELETE_FILM_GENRES, film.getId());
            insertFilmGenres(film);
        }

        return true;
    }

    @Override
    public List<Film> getMostPopular(int count) {
        return jdbcTemplate.query(SQL_GET_MOST_POPULAR, FilmDbStorage::mapRow, count);
    }

    private static Map<String, Object> filmToMap(Film film) {
        Map<String, Object> filmMap = new HashMap<>();

        filmMap.put("name", film.getName());
        filmMap.put("description", film.getDescription());
        filmMap.put("release_date", film.getReleaseDate());
        filmMap.put("duration", film.getDuration());
        filmMap.put("mpa", film.getMpa().getId());

        return filmMap;
    }

    private static Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();

        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(new MpaRatingItem(rs.getInt("mpa"),rs.getString("mpa_name")));
        film.setGenres(getFilmGenresFromString(rs.getString("genres")));

        return film;
    }

    private static List<Genre> getFilmGenresFromString(String s) {
        if (s == null || s.isBlank()) {
            return Collections.emptyList();
        }

        return Arrays.stream(s.split(","))
                .map(oneGenre -> {
                    String[] parts = oneGenre.split(":");
                    return new Genre(Integer.parseInt(parts[0]), parts[1]);
                })
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Integer> getFilmGenres(Film film) {
        return jdbcTemplate.query(SQL_GET_FILM_GENRES,
                (rs, rowNum) -> rs.getInt("genre_id"),
                film.getId());
    }

    private void insertFilmGenres(Film film) {
        final List<Object[]> values = new ArrayList<>();
        final int filmId = film.getId();
        film.getGenres().forEach(g -> values.add(new Integer[] {filmId, g.getId()}));
        jdbcTemplate.batchUpdate(SQL_INSERT_FILM_GENRE, values);
    }
}