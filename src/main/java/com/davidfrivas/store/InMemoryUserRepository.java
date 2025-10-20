package com.davidfrivas.store;

import java.util.HashMap;

public class InMemoryUserRepository implements UserRepository {
    // Key: email; Value: User
    private HashMap<String, User> users;

    @Override
    public void save(User user) {
        users.put(user.getEmail(), user);
        System.out.println("User saved.");
    }
}
