package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.yandex.practicum.filmorate.model.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractDbStorage<T extends Item> implements Storage<T> {
    private static final String GET_ALL_SQL_TEMPLATE = "SELECT * FROM %s";
    private static final String GET_SQL_TEMPLATE = "SELECT * FROM %s WHERE id = ?";
    private static final String EXISTS_SQL_TEMPLATE = "SELECT NULL FROM %s WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final String tableName;

    private final String getAllSql;
    private final String getSql;
    private final String existsSql;

    public AbstractDbStorage(JdbcTemplate jdbcTemplate, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;

        getAllSql = String.format(GET_ALL_SQL_TEMPLATE, tableName);
        getSql = String.format(GET_SQL_TEMPLATE, tableName);
        existsSql = String.format(EXISTS_SQL_TEMPLATE, tableName);
    }

    public abstract RowMapper<T> getRowMapper();

    public abstract Map<String, Object> itemToMap(T item);

    @Override
    public List<T> getAll() {
        return jdbcTemplate.query(getAllSql, getRowMapper());
    }

    @Override
    public void create(T item) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(tableName)
                .usingGeneratedKeyColumns("id");
        item.setId(jdbcInsert.executeAndReturnKey(itemToMap(item)).intValue());
    }

    @Override
    public Optional<T> get(int id) {
        List<T> items = jdbcTemplate.query(getSql, getRowMapper(), id);
        if (items.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(items.get(0));
        }
    }

    @Override
    public boolean exists(int id) {
        return jdbcTemplate.queryForRowSet(existsSql, id).next();
    }
}