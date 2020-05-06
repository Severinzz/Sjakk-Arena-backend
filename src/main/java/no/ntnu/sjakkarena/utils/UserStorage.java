package no.ntnu.sjakkarena.utils;

import no.ntnu.sjakkarena.data.User;

import java.util.HashMap;

/**
 * Stores users indexed by their userId.
 */
public class UserStorage {

    private static HashMap<Integer, User> USERS = new HashMap<>();

    public static User getUser(int userId) {
        return USERS.get(userId);
    }

    public static void addUser(int userId, User user) {
        USERS.put(userId, user);
    }
}
