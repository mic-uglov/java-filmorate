package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController extends AbstractController<User> {
    @Autowired
    public UserController(UserStorage storage) {
        super(storage);
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}