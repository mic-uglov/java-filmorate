package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/films")
public class FilmController extends AbstractController<Film> {
    private static final int DESCRIPTION_MAX_LEN = 200;
    private static final LocalDate RELEASE_MIN_DATE = LocalDate.of(1895, 12, 28);

    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);

    @Override
    public boolean validate(Film film) {
        boolean valid = true;

        if (film.getName() == null || film.getName().isBlank()) {
            getLogger().warn("Наименование фильма не должно быть пустым");
            valid = false;
        }
        if (film.getDescription() != null && film.getDescription().length() > DESCRIPTION_MAX_LEN) {
            getLogger().warn("Описание фильма не должно быть длиннее {} символов", DESCRIPTION_MAX_LEN);
            valid = false;
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(RELEASE_MIN_DATE)) {
            getLogger().warn("Дата выхода фильма не должна быть ранее {}",
                    RELEASE_MIN_DATE.format(DateTimeFormatter.ISO_DATE));
            valid = false;
        }
        if (film.getDuration() <= 0) {
            getLogger().warn("Длительность фильма должна быть положительной");
            valid = false;
        }

        return valid;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}