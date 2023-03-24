package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Getter
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid User user) {
        log.info("Получен POST запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                user);

        try {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(User.generateId());
        users.put(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity<>(
                    HttpStatus.BAD_GATEWAY);
        }
    }

    @PutMapping
    public ResponseEntity<?> putUser(@RequestBody User user) {
        log.info("Получен PUT запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                user);
        if (!users.containsKey(user.getId())) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "User with id " + user.getId() + " not found"),
                    HttpStatus.NOT_FOUND);
        }
        users.put(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}