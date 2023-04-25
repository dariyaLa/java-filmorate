package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ExceptionNotFound;
import ru.yandex.practicum.filmorate.inMemory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
public class FilmService {

    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    UserService userService;

    @Autowired
    public FilmService(UserService userService) {
        this.userService = userService;
    }

    public Film addLike(int idFilm, int idUser) {
        Map<Integer, Film> films = inMemoryFilmStorage.getFilms();
        Map<Integer, User> users = userService.getInMemoryUserStorage().getUsers();
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

    public void deleteLike(int idFilm, int idUser) {
        Map<Integer, Film> films = inMemoryFilmStorage.getFilms();
        Map<Integer, User> users = userService.getInMemoryUserStorage().getUsers();
        if (films.get(idFilm) == null || users.get(idUser) == null) {
            throw new ExceptionNotFound(String.format("Не найден Film c id %s или User с id %s", idFilm, idUser));
        }
        if (films.get(idFilm).getLikes() != null) {
            films.get(idFilm).getLikes().remove(users.get(idUser).getLogin());
        }
    }

    public Collection<Film> popularFilmList(int count) {
        Map<Integer, Film> films = inMemoryFilmStorage.getFilms();
        List<Film> sortFilms = new ArrayList<Film>(films.values());
        Collections.sort(sortFilms, new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                if (o1.getLikes() == null && o2.getLikes() == null) {
                    if (o1.getId() < o2.getId()) {
                        return -1;
                    }
                    return 1;
                }
                if (o1.getLikes().isEmpty() && o2.getLikes() == null) {
                    if (o1.getId() < o2.getId()) {
                        return -1;
                    }
                    return 1;
                }
                if (o1.getLikes() == null && o2.getLikes().isEmpty()) {
                    if (o1.getId() < o2.getId()) {
                        return -1;
                    }
                    return 1;
                }
                if (o1.getLikes() == null && !o2.getLikes().isEmpty()) {
                    return 1;
                }
                if (!o1.getLikes().isEmpty() && o2.getLikes() == null) {
                    return -1;
                }
                return o1.getLikes().size() - o2.getLikes().size();
            }
        });

        if (sortFilms.size() < count) {
            return sortFilms;
        }
        return sortFilms.subList(0, count);
    }

    public Film findFilm(int id) {
        if (inMemoryFilmStorage.getFilms().get(id) == null) {
            throw new ExceptionNotFound("Не найден Film c id " + id);
        }
        return inMemoryFilmStorage.getFilms().get(id);
    }

    public Film createFilm(Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    public Film putFilm(Film film) {
        return inMemoryFilmStorage.putFilm(film);
    }

    public Collection<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }
}
