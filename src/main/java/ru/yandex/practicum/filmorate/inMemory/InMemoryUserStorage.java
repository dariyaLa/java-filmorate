package ru.yandex.practicum.filmorate.inMemory;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    @Getter
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        user.setId(User.generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User putUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

    @Override
    public Optional<User> getUser(int id) {
        return Optional.empty();
    }
}
