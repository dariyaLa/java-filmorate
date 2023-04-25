package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен POST запрос к эндпоинту: /films, Строка параметров запроса: '{}'",
                film);
        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) {
        log.info("Получен PUT запрос к эндпоинту: /films, Строка параметров запроса: '{}'",
                film);
        return filmService.putFilm(film);
    }

    @PutMapping("films/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен PUT запрос к эндпоинту: films/{id}/like/{userId}, Строка параметров запроса: '{}' , '{}'",
                id, userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен DELETE запрос к эндпоинту: /users, Строка параметров запроса: '{}' , '{}'",
                id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public Collection<Film> popularFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        log.info("Получен GET запрос к эндпоинту: /films/popular?count={count}, Строка параметров запроса: '{}'",
                count);
        return filmService.popularFilmList(count);
    }

    @GetMapping("/films/{id}")
    public Film findFilm(@PathVariable int id) {
        return filmService.findFilm(id);
    }

}