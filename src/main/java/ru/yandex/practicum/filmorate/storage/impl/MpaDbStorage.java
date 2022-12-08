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
    private static final String SQL_GET = "SELECT * FROM mpa_rating WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<MpaRatingItem> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, MpaDbStorage::mapRow);
    }

    @Override
    public Optional<MpaRatingItem> get(int id) {
        List<MpaRatingItem> mpas = jdbcTemplate.query(SQL_GET, MpaDbStorage::mapRow, id);
        if (mpas.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(mpas.get(0));
        }
    }

    private static MpaRatingItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MpaRatingItem(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"));
    }
}