package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ExceptionNotFound;
import ru.yandex.practicum.filmorate.inMemory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
public class FilmServiceMemory implements FilmService {

    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    UserServiceMemory userService;

    @Autowired
    public FilmServiceMemory(UserServiceMemory userService) {
        this.userService = userService;
    }

    public Film findFilm(int id) {
        if (inMemoryFilmStorage.getFilms().get(id) == null) {
            throw new ExceptionNotFound("Не найден Film c id " + id);
        }
        return inMemoryFilmStorage.getFilms().get(id);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return null;
    }

    @Override
    public Optional<Film> getFilm(int id) {
        return Optional.empty();
    }

    @Override
    public Collection<Film> popularFilm(int count) {
        return null;
    }

    @Override
    public void addLike(int idFilm, int idUser) {

    }

    @Override
    public void deleteLike(int filmId, int userId) {

    }

    @Override
    public Collection<Mpa> getMpa() {
        return null;
    }

    @Override
    public Mpa getMpaId(int mpaId) {
        return null;
    }

    @Override
    public Collection<Genre> getGenres() {
        return null;
    }

    @Override
    public Genre getGenre(int genreId) {
        return null;
    }

    public Film createFilm(Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    public Film putFilm(Film film) {
        return inMemoryFilmStorage.putFilm(film);
    }

    public Collection<Film> findAll() {
        return inMemoryFilmStorage.getFilms().values();
    }
}
