package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service("userServiceDB")
public class UserServiceDB implements UserService {

    private final UserDbStorage userStorage;

    public UserServiceDB(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public Map<Integer, User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public User putUser(User user) {
        return userStorage.putUser(user);
    }

    @Override
    public void addFriend(int idUser, int idFriendUser) {
        String sql = "insert into follows (following_user_id,followed_user_id,status) values (?,?,?)";
        userStorage.getJdbcTemplate().update(sql,
                        idUser,
                        idFriendUser,
                        false
                );
    }

    @Override
    public Optional<User> getUser(int id) {
        return userStorage.getUser(id);
    }

    @Override
    public Collection<User> findCommonFriend(int id, int otherId) {
        Map<Integer, User> users = new HashMap<>();
        String sql = "select * from users where id in(" +
                "select followed_user_id from follows where following_user_id=? and " +
                "followed_user_id in (select followed_user_id from FOLLOWS where following_user_id=?))";
        SqlRowSet userRows = userStorage.getJdbcTemplate().queryForRowSet(sql, id, otherId);
        return users(userRows).values();
    }

    @Override
    public Collection<User> findFriend(int idUser) {
        Map<Integer, User> users = new HashMap<>();
        String sql = "select * from users where id in(" +
                "SELECT followed_user_id as friends from follows where following_user_id=? and status=false\n" +
                "UNION\n" +
                "select following_user_id as friends from follows where followed_user_id=? and status=true)";
        SqlRowSet userRows = userStorage.getJdbcTemplate().queryForRowSet(sql, idUser, idUser);
        return users(userRows).values();
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sql = "delete from follows where following_user_id=? and followed_user_id=?";
        userStorage.getJdbcTemplate().update(sql, id, friendId);
    }

    //собираем результат из БД в мапу
    private Map<Integer, User> users(SqlRowSet userRows) {
        Map<Integer, User> users = new HashMap<>();
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
}
