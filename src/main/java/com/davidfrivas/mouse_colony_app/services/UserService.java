package com.davidfrivas.mouse_colony_app.services;

import com.davidfrivas.mouse_colony_app.entities.Lab;
import com.davidfrivas.mouse_colony_app.entities.User;
import com.davidfrivas.mouse_colony_app.repositories.LabRepository;
import com.davidfrivas.mouse_colony_app.repositories.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final LabRepository labRepository;

    // Create a new user
    public User createUser(User user, Long labId) {
        // Validation
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }

        // Fetch and set the lab
        Lab lab = labRepository.findById(labId).orElseThrow(() -> new IllegalArgumentException("Lab not found with id " + labId));
        user.setLab(lab);

        return userRepository.save(user); // Save user to repository
    }

    // Find user by ID
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    // Get all users
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    // Update existing user
    public User updateUser(Long id, User updatedUser) {
        User existingUser = findById(id);

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setName(updatedUser.getName());
        existingUser.setRole(updatedUser.getRole());
        // Password updated in separate method

        return userRepository.save(existingUser);
    }

    // Update user's lab
    public User updateUserLab(Long userId, Long newLabId) {
        User user = findById(userId);
        Lab newLab = labRepository.findById(newLabId).orElseThrow(() -> new RuntimeException("Lab not found with id " + newLabId));

        user.setLab(newLab); // Set the new lab
        return userRepository.save(user);
    }

    // Update password (setting up encryption with BCrypt later)
    public User updatePassword(Long userId, String newPassword) {
        User user = findById(userId);
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(Long userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }

    // Check if user exists
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
