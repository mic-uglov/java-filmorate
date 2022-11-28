package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class FilmDeserializer extends JsonDeserializer<Film> {
    private final FilmService filmService;

    @Autowired
    public FilmDeserializer(FilmService filmService) {
        this.filmService = filmService;
    }

    @Override
    public Film deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        Film film = new Film();

        JsonNode idNode = node.get("id");
        if (idNode != null) {
            film.setId(idNode.asInt());
        }

        JsonNode nameNode = node.get("name");
        if (nameNode != null) {
            film.setName(nameNode.asText());
        }

        JsonNode descriptionNode = node.get("description");
        if (descriptionNode != null) {
            film.setDescription(descriptionNode.asText());
        }

        JsonNode releaseDateNode = node.get("releaseDate");
        if (releaseDateNode != null) {
            film.setReleaseDate(LocalDate.parse(releaseDateNode.asText()));
        }

        JsonNode durationNode = node.get("duration");
        if (durationNode != null) {
            film.setDuration(durationNode.asInt());
        }

        JsonNode mpaNode = node.get("mpa");
        if (mpaNode != null) {
            JsonNode mpaIdNode = mpaNode.get("id");
            if (mpaIdNode != null) {
                film.setMpa(filmService.getMpa(mpaIdNode.asInt()).orElseThrow());
            }
        }

        JsonNode genresNode = node.get("genres");
        if (genresNode != null && genresNode.getNodeType() == JsonNodeType.ARRAY) {
            Set<Genre> genres = new HashSet<>();
            genresNode.elements().forEachRemaining(genreNode -> {
                JsonNode genreIdNode = genreNode.get("id");
                if (genreIdNode != null) {
                    genres.add(filmService.getGenre(genreIdNode.asInt()).orElseThrow());
                }
            });
            film.setGenres(genres);
        }

        return film;
    }
}