package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Primary
public class FilmDbStorage implements FilmStorage {
    private static final String TABLE_NAME = "films";

    private static final String SQL_GET_ALL = "SELECT * FROM films";
    private static final String SQL_GET = "SELECT * FROM films WHERE id = ?";
    private static final String SQL_UPDATE =
            "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa = ? " +
                    "WHERE id = ?";
    // TODO
    private static final String SQL_EXISTS = "SELECT NULL FROM films WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    private final Map<Integer, MpaRatingItem> mpas;
    private final Map<Integer, Genre> genres;

    // TODO
    private static final Map<String, String> sqls = Map.of(
            "getMpas", "SELECT * FROM mpa_rating ORDER BY id",
            "getGenres", "SELECT * FROM genres ORDER BY id",
            "getFilmGenres", "SELECT genre_id FROM film_genre WHERE film_id = ? ORDER BY genre_id",
            "deleteFilmGenres", "DELETE FROM film_genre WHERE film_id = ?",
            "insertFilmGenre", "INSERT INTO film_genre VALUES (?, ?)",
            "putALike",
                    "INSERT INTO likes SELECT ?, ? " +
                            "WHERE NOT EXISTS (" +
                                "SELECT NULL FROM likes WHERE film_id = ? AND user_id = ?)",
            "removeALike", "DELETE FROM likes WHERE film_id = ? AND user_id = ?",
            "getMostPopular",
                    "SELECT f.* FROM films f LEFT JOIN likes l ON l.film_id = f.id " +
                            "GROUP BY f.id ORDER BY COUNT(l.user_id) DESC LIMIT ?"
    );

    private static Map<String, Object> filmToMap(Film film) {
        Map<String, Object> filmMap = new HashMap<>();

        filmMap.put("name", film.getName());
        filmMap.put("description", film.getDescription());
        filmMap.put("release_date", film.getReleaseDate());
        filmMap.put("duration", film.getDuration());
        filmMap.put("mpa", film.getMpa().getId());

        return filmMap;
    }

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        mpas = new TreeMap<>();
        cacheMpas();

        genres = new TreeMap<>();
        cacheGenres();
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

    private void cacheMpas() {
        List<MpaRatingItem> mpaList = jdbcTemplate.query(
                sqls.get("getMpas"),
                (rs, rowNum) -> new MpaRatingItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")));
        mpaList.forEach(mpa -> mpas.put(mpa.getId(), mpa));
    }

    private void cacheGenres() {
        List<Genre> genresList = jdbcTemplate.query(
                sqls.get("getGenres"),
                (rs, rowNum) -> new Genre(
                        rs.getInt("id"),
                        rs.getString("name")));
        genresList.forEach(genre -> genres.put(genre.getId(), genre));
    }

    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();

        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(mpas.get(rs.getInt("mpa")));
        film.setGenres(getFilmGenres(film));

        return film;
    }

    private List<Genre> getFilmGenres(Film film) {
        return jdbcTemplate.query(
                sqls.get("getFilmGenres"),
                (rs, rowNum) -> genres.get(rs.getInt("genre_id")), film.getId());

    }

    @Override
    public void create(Film film) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        film.setId(jdbcInsert.executeAndReturnKey(filmToMap(film)).intValue());
        // TODO
        // жанры надо добавить batchUpdate
        // причем, видимо, через службу?
    }

    @Override
    public void update(Film film) {
        jdbcTemplate.update(SQL_UPDATE,
                film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa());
        if (!film.getGenres().equals(getFilmGenres(film))) {
            jdbcTemplate.update(sqls.get("deleteFilmGenres"), film.getId());
            // TODO с жанрами будет работать по-другому
            insertFilmGenres(film);
        }
    }

    private void insertFilmGenres(Film film) {
        int filmId = film.getId();
        String insertSql = sqls.get("insertFilmGenre");
        film.getGenres().forEach(
                genre -> jdbcTemplate.update(insertSql, filmId, genre.getId())
        );
    }

    @Override
    public void putALike(int filmId, int userId) {
        jdbcTemplate.update(sqls.get("putALike"), filmId, userId, filmId, userId);
    }

    @Override
    public void removeALike(int filmId, int userId) {
        jdbcTemplate.update(sqls.get("removeALike"), filmId, userId);
    }

    @Override
    public List<Film> getMostPopular(int count) {
        return jdbcTemplate.query(sqls.get("getMostPopular"), this::mapRow, count);
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