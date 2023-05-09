package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmIntegrationTest {

    @Autowired
    private final FilmDbStorage filmStorage;

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FilmController controller;

    @Test
    public void testFindFilmByIdTest() {

        Optional<Film> userOptional = filmStorage.getFilm(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @BeforeEach
    public void init() {
        String sql = "insert into films (id,name,description,releaseDate,duration,mpa_id) " +
                "values (1,'filmname','filmdescription','2023-01-23',100,1)";
        jdbcTemplate.update(sql);
    }

    @AfterEach
    public void clean() {
        String sql = "delete from films";
        jdbcTemplate.update(sql);
    }

}