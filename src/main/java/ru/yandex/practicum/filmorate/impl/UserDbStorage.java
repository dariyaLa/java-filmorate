package ru.yandex.practicum.filmorate.impl;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Qualifier("userDBStorage")
public class UserDbStorage implements UserStorage {

    @Getter
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Getter
    public Map<Integer, User> users = new HashMap<>();

    @Override
    public Map<Integer, User> getUsers() {
        String sql = "select * from users";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql);
        while (userRows.next()) {
            users.put(userRows.getInt("id"), User.builder()
                    .id(userRows.getInt("id"))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("username"))
                    .birthday(userRows.getDate("birthday").toLocalDate())
                    .build());
        }
        return users;
    }

    public Optional<User> getUser(int id) {
        String sql = "select * from users where id=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (userRows.next()) {
            return Optional.ofNullable(User.builder()
                    .id(userRows.getInt("id"))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("username"))
                    .birthday(userRows.getDate("birthday").toLocalDate())
                    .build());
        }
        return Optional.empty();
    }

    @Override
    public User createUser(User user) {
        int id = 0;
        String sqlGetId = "select max(id) as id from users";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlGetId);
        userRows.next();
        id = userRows.getInt("id") + 1;
        checkName(user); //редактируем name, если оно пустое
        String sql = "insert into users (id,username,login,email,birthday) values (?,?,?,?,?)";
        jdbcTemplate.update(sql,
                        id,
                        user.getName(),
                        user.getLogin(),
                        user.getEmail(),
                        user.getBirthday());

        user.setId(id);
        return user;
    }

    @Override
    public User putUser(User user) {
        String sql = "update users set  username=?, login=?, email=?, birthday=? where id=?";
        jdbcTemplate.update(sql,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        return getUser(user.getId()).get();
    }

    public void checkName(User user) {
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
        }
    }
}
