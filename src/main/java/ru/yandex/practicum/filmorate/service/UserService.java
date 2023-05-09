package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    void addFriend(int idUser, int idFriendUser);

    User createUser(User user);

    Map<Integer, User> getUsers();

    User putUser(User user);

    Optional<User> getUser(int user);

    Collection<User> findCommonFriend(int id, int otherId);

    Collection<User> findFriend(int id);

    void deleteFriend(int id, int friendId);
}
