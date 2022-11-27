package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.yandex.practicum.filmorate.model.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractDbStorage<T extends Item> implements Storage<T> {
    private final JdbcTemplate jdbcTemplate;

    public AbstractDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected abstract String getSql(String key);

    protected abstract RowMapper<T> getRowMapper();

    protected abstract String getTableName();

    protected abstract Map<String, Object> itemToMap(T item);

    @Override
    public List<T> getAll() {
        return jdbcTemplate.query(getSql("getAll"), getRowMapper());
    }

    @Override
    public void create(T item) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(getTableName())
                .usingGeneratedKeyColumns("id");
        item.setId(jdbcInsert.executeAndReturnKey(itemToMap(item)).intValue());
    }

    @Override
    public Optional<T> get(int id) {
        List<T> items = jdbcTemplate.query(getSql("get"), getRowMapper(), id);
        if (items.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(items.get(0));
        }
    }

    @Override
    public boolean exists(int id) {
        return jdbcTemplate.queryForRowSet(getSql("exists"), id).next();
    }
}