package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ExceptionNotFound;
import ru.yandex.practicum.filmorate.inMemory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.inMemory.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService,
                          InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен POST запрос к эндпоинту: /films, Строка параметров запроса: '{}'",
                film);
        return inMemoryFilmStorage.createFilm(film);
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) {
        log.info("Получен PUT запрос к эндпоинту: /films, Строка параметров запроса: '{}'",
                film);
//        if (!inMemoryFilmStorage.getFilms().containsKey(film.getId())){
//            throw new ExceptionNotFound(String.format("Не найден Film c id %s", film.getId()));
//        }
       return inMemoryFilmStorage.putFilm(film);
    }

    @PutMapping("films/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен PUT запрос к эндпоинту: films/{id}/like/{userId}, Строка параметров запроса: '{}' , '{}'",
                id, userId);
       return filmService.addLike(id, userId, inMemoryFilmStorage, inMemoryUserStorage);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен DELETE запрос к эндпоинту: /users, Строка параметров запроса: '{}' , '{}'",
                id, userId);
        filmService.deleteLike(id, userId, inMemoryFilmStorage, inMemoryUserStorage);
    }

    @GetMapping("/films/popular")
    public Collection<Film> popularFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        log.info("Получен GET запрос к эндпоинту: /films/popular?count={count}, Строка параметров запроса: '{}'",
                count);
        return filmService.popularFilmList(count, inMemoryFilmStorage);
    }

    @GetMapping("/films/{id}")
    public Film findFilm(@PathVariable int id) {
        return filmService.findFilm(id, inMemoryFilmStorage);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> exeptionHandle(final ExceptionNotFound e) {
        return new ResponseEntity<Map<String, String>>(
                Map.of("messageError", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

}