package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController extends AbstractController<Film> {
    @Autowired
    public FilmController(FilmStorage storage) {
        super(storage);
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}