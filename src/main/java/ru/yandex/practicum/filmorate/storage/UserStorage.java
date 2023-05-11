package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.Optional;

public interface UserStorage {

    Map<Integer, User> getUsers();

    User createUser(User user);

    User putUser(User user);

    Optional<User> getUser(int id);
}
