package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ExceptionNotFound;
import ru.yandex.practicum.filmorate.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service("filmServiceDB")
public class FilmServiceDB implements FilmService {

    private final FilmDbStorage filmStorage;

    public FilmServiceDB(FilmDbStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    @Override
    public Film putFilm(Film film) {
        return filmStorage.putFilm(film);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return filmStorage.getFilms();
    }

    @Override
    public Optional<Film> getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    @Override
    public Collection<Film> popularFilm(int count) {
        String sql = "select max(film_id) from (select film_id, count(film_id) from likes_film group by film_id)";
        SqlRowSet filmRows = filmStorage.getJdbcTemplate().queryForRowSet(sql);
        filmRows.next();
        int id = filmRows.getInt("max(film_id)");
        if (id != 0) {
            Collection<Film> popularFilm = new ArrayList<>();
            popularFilm.add(getFilm(id).get());
            return popularFilm;
      }
        return filmStorage.getFilms().values();
    }

    @Override
    public void addLike(int idFilm, int idUser) {
        String sql = "insert into likes_film (film_id,user_id) values (?,?)";
        filmStorage.getJdbcTemplate().update(sql,
                        idFilm,
                        idUser
                );
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sql = "select * from likes_film where film_id=? and user_id=?";
        SqlRowSet filmRows = filmStorage.getJdbcTemplate().queryForRowSet(sql, filmId, userId);
        if (!filmRows.next()) {
            throw new ExceptionNotFound("Не найдена запись с User id = " + userId + " и с Film id = " + filmId);
        } else {
            String sqlDelete = "delete from likes_film where film_id=? and user_id=?";
            filmStorage.getJdbcTemplate().update(sqlDelete,
                            filmId,
                            userId
                    );
        }
    }

    @Override
    public Collection<Mpa> getMpa() {
        return filmStorage.getMpa().values();
    }

    @Override
    public Mpa getMpaId(int mpaId) {
        String sql = "select * from mpa where mpa_id=?";
        SqlRowSet rows = filmStorage.getJdbcTemplate().queryForRowSet(sql, mpaId);
        if (!rows.next()) {
            throw new ExceptionNotFound("Не найдена запись с id = " + mpaId);
        }
        return new Mpa(rows.getInt("mpa_id"), rows.getString("rating"));
    }

    @Override
    public Collection<Genre> getGenres() {
        return filmStorage.getGenres().values();
    }

    @Override
    public Genre getGenre(int genreId) {
        String sql = "select * from genres where id=?";
        SqlRowSet rows = filmStorage.getJdbcTemplate().queryForRowSet(sql, genreId);
        if (!rows.next()) {
            throw new ExceptionNotFound("Не найдена запись с id = " + genreId);
        }
        return new Genre(rows.getInt("id"), rows.getString("genre"));
    }
}
