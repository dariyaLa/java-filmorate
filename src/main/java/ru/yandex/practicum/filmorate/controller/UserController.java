package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ExceptionNotFound;
import ru.yandex.practicum.filmorate.exeption.IncorrectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.info("Получен GET запрос к эндпоинту: /users");
        return userService.findAll();
    }

    @PostMapping("/users")
    public User create(@RequestBody @Valid User user) {

        log.info("Получен POST запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                user);
        userService.createUser(user);
        return user;
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) {
        log.info("Получен PUT запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                user);
        if (userService.putUser(user) == null) {
            throw new IncorrectException("Не найден User c id " + user.getId());
        }
        return userService.putUser(user);

    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен PUT запрос к эндпоинту: /users, Строка параметров запроса: '{}' , '{}'",
                id, friendId);
        if (!userService.getInMemoryUserStorage().getUsers().containsKey(id)
                || !userService.getInMemoryUserStorage().getUsers().containsKey(friendId)) {
            throw new ExceptionNotFound(String.format("Не найден User c id %s или %s", id, friendId));
        }
        userService.addFriend(id, friendId);

    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен DELETE запрос к эндпоинту: /users, Строка параметров запроса: '{}' , '{}'",
                id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> findFriend(@PathVariable int id) {
        return userService.findFriend(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findCommonFriend(@PathVariable int id, @PathVariable int otherId) {
        return userService.findCommonFriend(id, otherId);
    }

    @GetMapping("/users/{id}")
    public User findUser(@PathVariable int id) {
        if (userService.getInMemoryUserStorage().getUsers().get(id) == null) {
            throw new ExceptionNotFound("Не найден User c id " + id);
        }
        return userService.findUser(id);
    }

}