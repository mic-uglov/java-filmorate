package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

@SuppressWarnings("rawtypes")
@SpringBootTest
@AutoConfigureTestDatabase
public class AbstractDbStorageTest extends AbstractStorageTest {
    private static AbstractDbStorage storage;

    private static JdbcTemplate jdbcTemplate;

    private Item rowMap(ResultSet rs, int rowNum) throws SQLException {
        Item item = new Item();

        item.setId(rs.getInt(1));

        return item;
    }

    @Autowired
    public AbstractDbStorageTest(JdbcTemplate jdbcTemplate) {
        if (storage == null) {
            storage = mock(AbstractDbStorage.class,
                    withSettings()
                            .useConstructor(jdbcTemplate, "test_items")
                            .defaultAnswer(CALLS_REAL_METHODS));
            when(storage.getRowMapper()).thenReturn(this::rowMap);
        }

        if (AbstractDbStorageTest.jdbcTemplate == null) {
            AbstractDbStorageTest.jdbcTemplate = jdbcTemplate;
            jdbcTemplate.execute("CREATE TABLE test_items (id IDENTITY PRIMARY KEY)");
        }
    }

    @BeforeEach
    public void deleteItems() {
        jdbcTemplate.update("DELETE FROM test_items");
    }

    @Override
    protected Storage getStorage() {
        return storage;
    }

    @Test
    @Override
    public void testUpdating() {}

    @Test
    @Override
    public void testUpdatingWhenNotExists() {}
}