package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

    @Autowired
    private final UserDbStorage userStorage;

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserController controller;

    @Test
    @DisplayName("Проверка загрузки контекста")
    public void contextLoadsTest() throws Exception {
        assertThat(controller).isNotNull();
    }


    @Test
    public void testFindUserByIdTest() {

        Optional<User> userOptional = userStorage.getUser(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @BeforeEach
    public void init() {
        String sql = "insert into users (id,username,login,email,birthday) " +
                "values (1,'username','userlogin','useremail','1980-08-20')";
        jdbcTemplate.update
                (sql);
    }

    @AfterEach
    public void clean() {
        String sql = "delete from users";
        jdbcTemplate.update
                (sql);
    }
}