package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.inMemory.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    //добавление в друзья
    public void addFriend(int idUser, int idFriendUser, InMemoryUserStorage inMemoryUserStorage) {
        Map<Integer, User> users = inMemoryUserStorage.getUsers();

        if (users.containsKey(idUser) &&
                users.containsKey(idFriendUser)) {
            if (users.get(idUser).getFriends() == null) {
                HashSet<String> tempHashSet = new HashSet<>();
                tempHashSet.add(users.get(idFriendUser).getLogin());
                users.get(idUser).setFriends(tempHashSet);

            } else {
                users.get(idUser).getFriends()
                        .add(users.get(idFriendUser).getLogin());

            }
            if (users.get(idFriendUser).getFriends() == null) {
                HashSet<String> tempHashSet = new HashSet<>();
                tempHashSet.add(users.get(idUser).getLogin());
                users.get(idFriendUser).setFriends(tempHashSet);
            } else {
                users.get(idFriendUser).getFriends()
                        .add(users.get(idUser).getLogin());
            }
        }
    }

    public void deleteFriend(int idUser, int idFriendUser, InMemoryUserStorage inMemoryUserStorage) {
        Map<Integer, User> users = inMemoryUserStorage.getUsers();

        if (users.containsKey(idUser) &&
                users.containsKey(idFriendUser)) {
            if (users.get(idUser).getFriends() == null) {
                return;
            } else {
                users.get(idUser).getFriends()
                        .remove(users.get(idFriendUser).getLogin());
            }
            if (users.get(idFriendUser).getFriends() == null) {
                return;
            } else {
                users.get(idFriendUser).getFriends()
                        .remove(users.get(idUser).getLogin());
            }
        }
    }

    public List<User> findFriend(int idUser, InMemoryUserStorage inMemoryUserStorage) {
        Map<Integer, User> users = inMemoryUserStorage.getUsers();
        List<String> listFriends = new ArrayList<>(inMemoryUserStorage.getUsers().get(idUser).getFriends());
        List<User> userListFriends = users.values().stream()
                .filter(i -> listFriends.contains(i.getLogin())).collect(Collectors.toList());
        return userListFriends;

    }

    public List<User> findCommonFriend(int idUser, int idFriendUser, InMemoryUserStorage inMemoryUserStorage) {
        Map<Integer, User> users = inMemoryUserStorage.getUsers();
        if (users.containsKey(idUser) &&
                users.containsKey(idFriendUser)) {
            if (users.get(idUser).getFriends() != null &&
                    users.get(idFriendUser).getFriends() != null) {

                List<String> friends = users.get(idUser).getFriends().stream()
                        .filter(i -> users.get(idFriendUser).getFriends().contains(i))
                        .collect(Collectors.toList());
                List<User> userList = users.values().stream()
                        .filter(i -> friends.contains(i.getLogin())).collect(Collectors.toList());
                return userList;
            }
        }
        return new ArrayList<>();
    }

    public User findUser(int id, InMemoryUserStorage inMemoryUserStorage) {
        return inMemoryUserStorage.getUsers().get(id);
    }
}
