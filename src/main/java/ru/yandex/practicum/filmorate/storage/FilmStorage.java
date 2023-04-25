package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Map<Integer, Film> getFilms();

    Film createFilm(Film film);

    Film putFilm(Film film);

    Collection<Film> findAll();
}
