package ru.yandex.practicum.filmorate.inMemory;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ExceptionNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    @Getter
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(film.generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film putFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ExceptionNotFound(String.format("Не найден Film c id %s", film.getId()));
        }
    }
}
