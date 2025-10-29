package com.davidfrivas.store;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {
    // Key: email; Value: User
    private final HashMap<String, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        if (user == null)
            throw new NullPointerException("User is null");
        System.out.println("Saving user: ");
        users.put(user.getEmail(), user);
    }

    @Override
    public User findByEmail(String email) {
        if (email == null || email.isEmpty())
            return null;
        return users.getOrDefault(email, null);
    }
}
