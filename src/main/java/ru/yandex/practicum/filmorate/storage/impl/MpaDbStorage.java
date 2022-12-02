package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class MpaDbStorage implements MpaStorage {
    private static final String SQL_GET_ALL = "SELECT * FROM mpa_rating ORDER BY id";

    private final JdbcTemplate jdbcTemplate;
    private final Map<Integer, MpaRatingItem> mpas;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpas = new TreeMap<>();
        refresh();
    }

    @Override
    public Collection<MpaRatingItem> getAll() {
        refresh();
        return mpas.values();
    }

    @Override
    public Optional<MpaRatingItem> get(int id) {
        return Optional.of(mpas.get(id));
    }

    private static MpaRatingItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MpaRatingItem(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"));
    }

    private void refresh() {
        mpas.clear();
        List<MpaRatingItem> filmGenres = jdbcTemplate.query(SQL_GET_ALL, MpaDbStorage::mapRow);
        filmGenres.forEach(g -> mpas.put(g.getId(), g));
    }
}