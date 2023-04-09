package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ExceptionNotFound;
import ru.yandex.practicum.filmorate.exeption.IncorrectException;
import ru.yandex.practicum.filmorate.inMemory.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    @Getter
    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService){
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.info("Получен GET запрос к эндпоинту: /users");
        return inMemoryUserStorage.findAll();
    }

    @PostMapping("/users")
    public User create(@RequestBody @Valid User user) {

        log.info("Получен POST запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                user);
        inMemoryUserStorage.createUser(user);
        return user;
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) {
        log.info("Получен PUT запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                user);
        if (inMemoryUserStorage.putUser(user) == null){
                throw new IncorrectException("Не найден User c id " + user.getId());
            }
        return inMemoryUserStorage.putUser(user);

    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен PUT запрос к эндпоинту: /users, Строка параметров запроса: '{}' , '{}'",
                id, friendId);
        if (!inMemoryUserStorage.getUsers().containsKey(id)
                || !inMemoryUserStorage.getUsers().containsKey(friendId)) {
            throw new ExceptionNotFound(String.format("Не найден User c id %s или %s", id, friendId));
        }
        userService.addFriend(id, friendId, inMemoryUserStorage);

    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен DELETE запрос к эндпоинту: /users, Строка параметров запроса: '{}' , '{}'",
                id, friendId);
        userService.deleteFriend(id, friendId, inMemoryUserStorage);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> findFriend(@PathVariable int id) {
        return userService.findFriend(id, inMemoryUserStorage);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findCommonFriend(@PathVariable int id, @PathVariable int otherId) {
        return userService.findCommonFriend(id, otherId, inMemoryUserStorage);
    }

    @GetMapping("/users/{id}")
    public User findUser(@PathVariable int id) {
        if (inMemoryUserStorage.getUsers().get(id) == null){
            throw new ExceptionNotFound("Не найден User c id " + id);
        }
        return userService.findUser(id, inMemoryUserStorage);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleHappinessOverflow(final ExceptionNotFound e) {
        return new ResponseEntity<Map<String, String>>(
                Map.of("messageError", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

}