package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class InMemoryUserStorage extends InMemoryAbstractStorage<User> implements UserStorage {
    @Override
    protected void validate(User user) {
        // TODO
    }
}
