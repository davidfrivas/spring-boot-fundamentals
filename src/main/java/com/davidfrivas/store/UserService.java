package com.davidfrivas.store;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public UserService(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public void registerUser(User user) {
        if (user == null)
            throw new NullPointerException("User is null");

        String email = user.getEmail();

        if (userRepository.findByEmail(email) != null)
            throw new IllegalArgumentException("User with email '" + email + "' already exists.");

        userRepository.save(user); // Register user to repository
        notificationService.send("User registered: ", user.getEmail());
    }
}
