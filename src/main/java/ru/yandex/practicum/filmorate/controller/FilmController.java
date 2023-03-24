package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @Getter
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен POST запрос к эндпоинту: /films, Строка параметров запроса: '{}'",
                film);
        film.setId(Film.generateId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public ResponseEntity<?> putFilm(@RequestBody Film film) {
        log.info("Получен PUT запрос к эндпоинту: /films, Строка параметров запроса: '{}'",
                film);
        if (!films.containsKey(film.getId())) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Film with id " + film.getId() + " not found"),
                    HttpStatus.NOT_FOUND);
        }
        films.put(film.getId(), film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }
}