package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getUsers();

    User createUser(User user);

    User putUser(User user);

    Collection<User> findAll();
}
