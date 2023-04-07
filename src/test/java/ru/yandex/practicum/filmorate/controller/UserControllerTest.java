package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserController controller;

    @Test
    @DisplayName("Проверка загрузки контекста")
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("Проверка некорректный email")
    void create() {


        User user = User.builder()
                .id(1)
                .email("testtest.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(1999, 12, 12))
                .build();

        ResponseEntity<User> response = restTemplate.postForEntity("http://localhost:" + port + "/users",
                user, User.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}