package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryFilmStorageTest {
    private static InMemoryFilmStorage getDummyStorage() {
        InMemoryFilmStorage storage = new InMemoryFilmStorage();

        storage.create(new Film());
        storage.create(new Film());

        return storage;
    }

    @Test
    public void testRatingWhenNoLikes() {
        InMemoryFilmStorage storage = getDummyStorage();

        assertEquals(1, storage.getMostPopular(100).get(0).getId());
        assertEquals(2, storage.getMostPopular(100).get(1).getId());
    }

    @Test
    public void testRatingWhenOneWithLikesAndAnotherWithoutLikes() {
        InMemoryFilmStorage storage = getDummyStorage();

        storage.putALike(2, 1);

        assertEquals(List.of(storage.get(2), storage.get(1)), storage.getMostPopular(100));
    }

    @Test
    public void testRatingWhenEqualLikesNumber() {
        InMemoryFilmStorage storage = getDummyStorage();

        storage.putALike(1, 1);
        storage.putALike(2, 1);

        assertEquals(List.of(storage.get(1), storage.get(2)), storage.getMostPopular(100));
    }

    @Test
    public void testRatingAfterRemovingLikes() {
        InMemoryFilmStorage storage = getDummyStorage();

        storage.putALike(1, 1);
        storage.putALike(1, 2);
        storage.putALike(1, 3);

        storage.putALike(2, 1);
        storage.putALike(2, 2);

        assertEquals(List.of(storage.get(1), storage.get(2)), storage.getMostPopular(100));

        storage.removeALike(1, 1);
        storage.removeALike(1, 2);

        assertEquals(List.of(storage.get(2), storage.get(1)), storage.getMostPopular(100));
    }
}