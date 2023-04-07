package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {

    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FilmController controller;

    @Test
    @DisplayName("Проверка запроса без наименования")
    void create() {
        Film film = Film.builder()
                .id(1)
                .description("test")
                .releaseDate(LocalDate.of(1999, 12, 12))
                .build();

        ResponseEntity<Film> response = restTemplate.postForEntity("http://localhost:" + port + "/films",
                film, Film.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

}