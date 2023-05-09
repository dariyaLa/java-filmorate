package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface FilmService {

    Film createFilm(Film film);

    Film putFilm(Film film);

    Map<Integer, Film> getFilms();

    Optional<Film> getFilm(int id);

    Collection<Film> popularFilm(int count);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    Collection<Mpa> getMpa();

    Mpa getMpaId(int mpaId);

    Collection<Genre> getGenres();

    Genre getGenre(int genreId);
}
