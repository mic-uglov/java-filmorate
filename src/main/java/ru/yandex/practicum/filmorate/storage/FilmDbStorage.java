package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Primary
public class FilmDbStorage implements FilmStorage {
    private static final String TABLE_NAME = "films";

    private static final String SQL_GET_ALL =
            "SELECT " +
                    "f.*, " +
                    "LISTAGG(fg.genre_id, ',') WITHIN GROUP (ORDER BY fg.genre_id) genres " +
                "FROM films f JOIN film_genre fg ON fg.film_id = f.id " +
                "GROUP BY f.id";
    private static final String SQL_GET =
            "SELECT " +
                    "f.*, " +
                    "LISTAGG(fg.genre_id, ',') WITHIN GROUP (ORDER BY fg.genre_id) genres " +
                "FROM films f JOIN film_genre fg ON fg.film_id = f.id " +
                "WHERE f.id = ?";
    private static final String SQL_UPDATE =
            "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa = ? " +
                    "WHERE id = ?";
    private static final String SQL_GET_FILM_GENRES =
            "SELECT genre_id FROM film_genre WHERE film_id = ? ORDER BY genre_id";
    private static final String SQL_DELETE_FILM_GENRES = "DELETE FROM film_genre WHERE film_id = ?";
    private static final String SQL_INSERT_FILM_GENRE = "INSERT INTO film_genre VALUES (?, ?)";
    private static final String SQL_GET_MOST_POPULAR =
            "SELECT f.*, " +
                    "LISTAGG(fg.genre_id, ',') WITHIN GROUP (ORDER BY fg.genre_id) genres " +
                "FROM films f " +
                    "JOIN film_genre fg ON fg.film_id = f.id " +
                    "LEFT JOIN likes l ON l.film_id = f.id " +
                "GROUP BY f.id " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?";
    // TODO
    private static final String SQL_EXISTS = "SELECT NULL FROM films WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage, MpaStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, this::mapRow);
    }

    @Override
    public Optional<Film> get(int id) {
        List<Film> films = jdbcTemplate.query(SQL_GET, this::mapRow, id);
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
    public void update(Film film) {
        jdbcTemplate.update(SQL_UPDATE,
                film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        if (!film.getGenres().equals(getFilmGenres(film))) {
            jdbcTemplate.update(SQL_DELETE_FILM_GENRES, film.getId());
            insertFilmGenres(film);
        }
    }

    @Override
    public List<Film> getMostPopular(int count) {
        return jdbcTemplate.query(SQL_GET_MOST_POPULAR, this::mapRow, count);
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

    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();

        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(mpaStorage.get(rs.getInt("mpa")).orElseThrow());
        film.setGenres(getFilmGenresByIds(rs.getString("genres")));

        return film;
    }

    private List<Genre> getFilmGenresByIds(String ids) {
        return Arrays.stream(ids.split(","))
                .map(id -> genreStorage.get(Integer.parseInt(id)).orElseThrow())
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Genre> getFilmGenres(Film film) {
        return jdbcTemplate.query(SQL_GET_FILM_GENRES,
                (rs, rowNum) -> genreStorage.get(rs.getInt("genre_id")).orElseThrow(),
                film.getId());
    }

    private void insertFilmGenres(Film film) {
        final List<Object[]> values = new ArrayList<>();
        film.getGenres().forEach(g -> values.add(new Integer[] {film.getId(), g.getId()}));
        jdbcTemplate.batchUpdate(SQL_INSERT_FILM_GENRE, values);
    }
}