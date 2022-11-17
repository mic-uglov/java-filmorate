package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController extends AbstractController<Film> {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        super(service);
        this.service = service;
    }

    @PutMapping("/{id}/like/{userId}")
    public void putALike(@PathVariable int id, @PathVariable int userId) {
        service.putALike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeALike(@PathVariable int id, @PathVariable int userId) {
        service.removeALike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopular(@RequestParam(required = false, defaultValue = "0") int count) {
        return service.getMostPopular(count);
    }
}