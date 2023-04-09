package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ExceptionNotFound;
import ru.yandex.practicum.filmorate.inMemory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.inMemory.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
public class FilmService {

    public Film addLike(int idFilm, int idUser, InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        Map<Integer, Film> films = inMemoryFilmStorage.getFilms();
        Map<Integer, User> users = inMemoryUserStorage.getUsers();
        if (films.get(idFilm) == null || users.get(idUser) == null) {
            throw new ExceptionNotFound(String.format("Не найден Film c id %s или User с id %s", idFilm, idUser));
        }
        if (films.get(idFilm).getLikes() == null) {
            HashSet<String> tempHashSet = new HashSet<>();
            tempHashSet.add(users.get(idUser).getLogin());
            films.get(idFilm).setLikes(tempHashSet);
            return films.get(idFilm);

        } else {
            films.get(idFilm).getLikes()
                    .add(users.get(idUser).getLogin());
            return films.get(idFilm);

        }
    }

    public void deleteLike(int idFilm, int idUser, InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        Map<Integer, Film> films = inMemoryFilmStorage.getFilms();
        Map<Integer, User> users = inMemoryUserStorage.getUsers();
        if (films.get(idFilm) == null || users.get(idUser) == null) {
            throw new ExceptionNotFound(String.format("Не найден Film c id %s или User с id %s", idFilm, idUser));
        }
        if (films.get(idFilm).getLikes() != null) {
            films.get(idFilm).getLikes().remove(users.get(idUser).getLogin());
        }
    }

    public Collection<Film> popularFilmList(int count, InMemoryFilmStorage inMemoryFilmStorage) {
        Map<Integer, Film> films = inMemoryFilmStorage.getFilms();
        List<Film> sortFilms = new ArrayList<Film>(films.values());
        Collections.sort(sortFilms);
        if (sortFilms.size() < count) {
            return sortFilms;
        }
        return sortFilms.subList(0, count);
    }

    public Film findFilm(int id, InMemoryFilmStorage inMemoryFilmStorage) {
        if (inMemoryFilmStorage.getFilms().get(id) == null) {
            throw new ExceptionNotFound("Не найден Film c id " + id);
        }
        return inMemoryFilmStorage.getFilms().get(id);
    }
}
