package service;

import model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {

    final static Map<Integer, User> users = new ConcurrentHashMap<>();

    static {
        users.put(1, new User("Hendricks", "Richard", "/img/photo-richard.png"));
        users.put(2, new User("Dunn", "Jared", "/img/photo-jared.png"));
        users.put(3, new User("Gilfoyle", "Bertram", "/img/photo-gilfoyle.png"));
        users.put(4, new User("Chugtai", "Dinesh", "/img/photo-dinesh.png"));
        users.put(5, new User("Hall", "Monica", "/img/photo-monica.png"));
    }

    public static Optional<User> queryById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    public static Optional<User> queryByCredentials(String username, String passwordHash) {
        return users.values().stream().filter(u -> u.name.equals(username)).findFirst();
    }

    public static Collection<User> queryAllUsers() {
        return users.values();
    }
}
