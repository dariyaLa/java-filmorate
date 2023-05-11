package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Map;
import java.util.Optional;

public interface FilmStorage {

    Map<Integer, Film> getFilms();

    Film createFilm(Film film);

    Film putFilm(Film film);

    Map<Integer, Genre> getGenres();

    Optional<Film> getFilm(int id);

    Map<Integer, Mpa> getMpa();
}
