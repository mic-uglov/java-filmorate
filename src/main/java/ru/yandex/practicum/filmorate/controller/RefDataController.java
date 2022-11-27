package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRatingItem;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Optional;

@RestController
public class RefDataController {
    private final FilmService filmService;

    @Autowired
    public RefDataController(FilmService service) {
        this.filmService = service;
    }

    @GetMapping("/genres")
    public Collection<Genre> getGenres() {
        return filmService.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Optional<Genre> getGenre(@PathVariable int id) {
        return filmService.getGenre(id);
    }

    @GetMapping("/mpa")
    public Collection<MpaRatingItem> getMpas() {
        return filmService.getMpas();
    }

    @GetMapping("/mpa/{id}")
    public Optional<MpaRatingItem> getMpa(@PathVariable int id) {
        return filmService.getMpa(id);
    }
}