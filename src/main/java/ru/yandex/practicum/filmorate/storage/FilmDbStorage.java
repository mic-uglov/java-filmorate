package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Primary
public class FilmDbStorage extends AbstractDbStorage<Film> implements FilmStorage {
    private static final String TABLE_NAME = "films";
    private static final Map<String, String> sqls = Map.of(
            "getMpas", "SELECT * FROM mpa_rating ORDER BY id",
            "getGenres", "SELECT * FROM genres ORDER BY id",
            "getFilmGenres", "SELECT genre_id FROM film_genre WHERE film_id = ? ORDER BY genre_id",
            "update", "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa = ?",
            "deleteFilmGenres", "DELETE FROM film_genre WHERE film_id = ?",
            "insertFilmGenre", "INSERT INTO film_genre VALUES (?, ?)"
    );

    private static Map<String, Object> filmToMap(Film film) {
        Map<String, Object> filmMap = new HashMap<>();

        filmMap.put("name", film.getName());
        filmMap.put("description", film.getDescription());
        filmMap.put("release_date", film.getReleaseDate());
        filmMap.put("duration", film.getDuration());
        MpaRatingItem mpa = film.getMpa();
        if (mpa == null) {
            filmMap.put("mpa", null);
        } else {
            filmMap.put("mpa", mpa.getId());
        }

        return filmMap;
    }

    private final JdbcTemplate jdbcTemplate;

    private final Map<Integer, MpaRatingItem> mpas;
    private final Map<Integer, Genre> genres;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, TABLE_NAME);
        this.jdbcTemplate = jdbcTemplate;

        mpas = new TreeMap<>();
        cacheMpas();

        genres = new TreeMap<>();
        cacheGenres();
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

    @Override
    protected RowMapper<Film> getRowMapper() {
        return this::mapRow;
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

    private Set<Genre> getFilmGenres(Film film) {
        return new HashSet<>(
                jdbcTemplate.query(
                        sqls.get("getFilmGenres"),
                        (rs, rowNum) -> genres.get(rs.getInt("genre_id")), film.getId())
        );
    }

    @Override
    protected Map<String, Object> itemToMap(Film film) {
        return filmToMap(film);
    }

    @Override
    public void create(Film film) {
        super.create(film);
        insertFilmGenres(film);
    }

    @Override
    public void update(Film film) {
        jdbcTemplate.update(sqls.get("update"),
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa() == null ? null : film.getMpa().getId());
        if (!film.getGenres().equals(getFilmGenres(film))) {
            jdbcTemplate.update(sqls.get("deleteFilmGenres"), film.getId());
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

    }

    @Override
    public void removeALike(int filmId, int userId) {

    }

    @Override
    public List<Film> getMostPopular(int count) {
        return null;
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