package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ExceptionNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
public class FilmController {

    @Qualifier("filmServiceDB")
    private final FilmService filmService;

    @Qualifier("userServiceDB")
    private final UserService userService;

    @Autowired
    public FilmController(@Qualifier("filmServiceDB") FilmService filmService, @Qualifier("userServiceDB") UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
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
        if (filmService.getFilm(film.getId()).isEmpty()) {
            throw new ExceptionNotFound("Не найден Film c id " + film.getId());
        }
        return filmService.putFilm(film);
    }

    @GetMapping("/films/{id}")
    public Optional<Film> findFilm(@PathVariable int id) {
        if (filmService.getFilm(id).isEmpty()) {
            throw new ExceptionNotFound("Не найден Film c id " + id);
        }
        return filmService.getFilm(id);
    }

    @GetMapping("/films")
    public Collection<Film> getFilms() {
        return filmService.getFilms().values();
    }

    @GetMapping("/films/popular")
    public Collection<Film> popularFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        log.info("Получен GET запрос к эндпоинту: /films/popular?count={count}, Строка параметров запроса: '{}'",
                count);
        return filmService.popularFilm(count);
    }

    @PutMapping("films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен PUT запрос к эндпоинту: films/{id}/like/{userId}, Строка параметров запроса: '{}' , '{}'",
                id, userId);
        if (userService.getUser(userId).isEmpty()) {
            throw new ExceptionNotFound("Не найден User c id " + userId);
        }
        if (filmService.getFilm(id).isEmpty()) {
            throw new ExceptionNotFound("Не найден Film c id " + id);
        }
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен DELETE запрос к эндпоинту: /users, Строка параметров запроса: '{}' , '{}'",
                id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/mpa")
    public Collection<Mpa> getMpa() {
        log.info("Получен GET запрос к эндпоинту: /mpa");
        return filmService.getMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaId(@PathVariable int id) {
        log.info("Получен GET запрос к эндпоинту: /mpa/{id}, параметр: '{}'", id);
        return filmService.getMpaId(id);
    }

    @GetMapping("/genres")
    public Collection<Genre> getGenres() {
        log.info("Получен GET запрос к эндпоинту: /genres");
        return filmService.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable int id) {
        log.info("Получен GET запрос к эндпоинту: /genres/{id}, параметр: '{}'", id);
        return filmService.getGenre(id);
    }
}