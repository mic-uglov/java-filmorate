package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.constraints.Past;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return service.getFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void becomeFriends(@PathVariable int id, @PathVariable int friendId) {
        service.becomeFriends(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void stopBeingFriends(@PathVariable int id, @PathVariable int friendId) {
        service.stopBeingFriends(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return service.getCommonFriends(id, otherId);
    }
}