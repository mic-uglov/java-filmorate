package ru.yandex.practicum.filmorate.test;

import ru.yandex.practicum.filmorate.storage.InMemoryAbstractStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import static org.mockito.Mockito.*;

@SuppressWarnings("rawtypes")
public class InMemoryAbstractStorageTest extends AbstractStorageTest {
    @Override
    protected Storage getStorage() {
        return mock(InMemoryAbstractStorage.class,
                withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }
}