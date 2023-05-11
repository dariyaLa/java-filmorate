package ru.yandex.practicum.filmorate.impl;

import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ExceptionNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component("filmDBStorage")
public class FilmDbStorage implements FilmStorage {

    @Getter
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        String sql = "select * from films";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql);
        return filmsBuilderMap(filmRows);
    }

    @Override
    public Optional<Film> getFilm(int id) {
        String sql = "select * from films where id=?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        return filmBuilder(filmRows);
    }

    @Override
    public Film createFilm(Film film) {
        int id = 0;
        String sqlGetId = "select max(id) as id from films";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlGetId);
        filmRows.next();
        id = filmRows.getInt("id") + 1;
        String sql = "insert into films (id,name,description,releaseDate,duration,mpa_id) values (?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                        id,
                        film.getName(),
                        film.getDescription(),
                        film.getReleaseDate(),
                        film.getDuration(),
                        film.getMpa().getId());
        film.setId(id);
        putGenreFilm(film);
        return film;
    }

    @Override
    public Film putFilm(Film film) {
        String sql = "update films set name=?, description=?, releaseDate=?, duration=?, mpa_id=? where id=?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        putGenreFilm(film); //вносим в БД жанр фильма
        return getFilm(film.getId()).get();
    }

    @Override
     public Map<Integer, Mpa> getMpa() {
        Map<Integer, Mpa> mpa = new HashMap<>();
        String sql = "select * from mpa";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
        while (rows.next()) {
            mpa.put(rows.getInt("mpa_id"),
                    new Mpa(rows.getInt("mpa_id"), rows.getString("rating"))
            );
        }
        return mpa;
    }

    @Override
    public Map<Integer, Genre> getGenres() {
        Map<Integer, Genre> genres = new HashMap<>();
        String sql = "select * from genres";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
        while (rows.next()) {
            genres.put(rows.getInt("id"),
                    new Genre(rows.getInt("id"), rows.getString("genre"))
            );
        }
        return genres;
    }

    //обновляем жанр для фильма
    private void putGenreFilm(Film film) {
        Collection<Integer> genresId = film.getGenres().stream()
                .map(Genre::getId)
                .distinct()
                .collect(Collectors.toList());
        //если пустое множество - удаляем все
        if (genresId.isEmpty()) {
            String sql = "delete from films_genres where film_id=?";
            jdbcTemplate.update(sql, film.getId());
        } else {
            String sqlDelete = "delete from films_genres where film_id=?";
            jdbcTemplate.update(sqlDelete,
                            film.getId());
            for (Integer i : genresId) {
                if (getGenre(i) != null) {
                    String sql = "insert into films_genres (film_id,genre_id) values (?,?)";
                    jdbcTemplate.update(sql,
                                    film.getId(),
                                    i);
                }
            }
        }
    }

    private List<Genre> getGenresForFilms(Optional<Film> film) {
        Map<Integer, Genre> genres = getGenres();
        List<Genre> genresFilm = new ArrayList<>();
        String sql = "select * from films_genres where film_id=?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, film.get().getId());
        while (rows.next()) {
            genresFilm.add(genres.get(rows.getInt("genre_id")));
        }
        return genresFilm;
    }

    private Mpa getMpaId(int mpaId) {
        String sql = "select * from mpa where mpa_id=?";
        SqlRowSet rows = getJdbcTemplate().queryForRowSet(sql, mpaId);
        if (!rows.next()) {
            throw new ExceptionNotFound("Не найдена запись с id = " + mpaId);
        }
        return new Mpa(rows.getInt("mpa_id"), rows.getString("rating"));
    }

    private Genre getGenre(int genreId) {
        String sql = "select * from genres where id=?";
        SqlRowSet rows = getJdbcTemplate().queryForRowSet(sql, genreId);
        if (!rows.next()) {
            throw new ExceptionNotFound("Не найдена запись с id = " + genreId);
        }
        return new Genre(rows.getInt("id"), rows.getString("genre"));
    }


    private Optional<Film> filmBuilder(SqlRowSet rows) {
        if (rows.next()) {
            Optional<Film> film = Optional.ofNullable(Film.builder()
                    .id(rows.getInt("id"))
                    .name(rows.getString("name"))
                    .description(rows.getString("description"))
                    .releaseDate(rows.getDate("releaseDate").toLocalDate())
                    .duration(rows.getLong("duration"))
                    .mpa(new Mpa(rows.getInt("mpa_id"), getMpa().get(rows.getInt("mpa_id")).getName()))
                    .build());
            film.get().setGenres(getGenresForFilms(film));
            return film;
        }
        return Optional.empty();
    }

    private Map<Integer, Film> filmsBuilderMap(SqlRowSet rows) {
        Map<Integer, Film> films = new HashMap<>();
        Film film;
        while (rows.next()) {
            films.put(rows.getInt("id"), film = Film.builder()
                    .id(rows.getInt("id"))
                    .name(rows.getString("name"))
                    .description(rows.getString("description"))
                    .releaseDate(rows.getDate("releaseDate").toLocalDate())
                    .duration(rows.getLong("duration"))
                    .mpa(new Mpa(rows.getInt("mpa_id"), getMpa().get(rows.getInt("mpa_id")).getName()))
                    .build());
            film.setGenres(getGenresForFilms(Optional.of(film)));
        }
        return films;
    }
}
