package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ExceptionNotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
public class UserController {
    @Autowired
    @Qualifier("userServiceDB")
    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("userServiceDB") UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User create(@RequestBody @Valid User user) {

        log.info("Получен POST запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                user);
        userService.createUser(user);
        return user;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.info("Получен GET запрос к эндпоинту: /users");
        return userService.getUsers().values();
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) {
        log.info("Получен PUT запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                user);
        if (userService.putUser(user) == null) {
            throw new ExceptionNotFound("Не найден User c id " + user.getId());
        }
        return userService.putUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен PUT запрос к эндпоинту: /users, Строка параметров запроса: '{}' , '{}'",
                id, friendId);
        if (!userService.getUsers().containsKey(id)
                || !userService.getUsers().containsKey(friendId)) {
            throw new ExceptionNotFound(String.format("Не найден User c id %s или %s", id, friendId));
        }
        userService.addFriend(id, friendId);
    }

    @GetMapping("/users/{id}")
    public Optional<User> findUser(@PathVariable int id) {
        if (userService.getUser(id).isEmpty()) {
            throw new ExceptionNotFound("Не найден User c id " + id);
        }
        return userService.getUser(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriend(@PathVariable int id, @PathVariable int otherId) {
        return userService.findCommonFriend(id, otherId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> findFriend(@PathVariable int id) {
        return userService.findFriend(id);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен DELETE запрос к эндпоинту: /users, Строка параметров запроса: '{}' , '{}'",
                id, friendId);
        userService.deleteFriend(id, friendId);
    }
}