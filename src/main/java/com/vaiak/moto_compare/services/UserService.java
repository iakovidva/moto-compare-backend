package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.exceptions.UserNotFoundException;
import com.vaiak.moto_compare.repositories.UserRepository;
import com.vaiak.moto_compare.security.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public Optional<User> findByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username).orElseThrow(UserNotFoundException::new);
    }

    public Optional<User> findByUserNameOptional(String username) {
        return userRepository.findByUserName(username);
    }

    public Optional<User> findByGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId);
    }

    public User createGoogleUser(String email, String googleId, String name, String profilePictureUrl) {
        User user = new User();
        user.setEmail(email);
        user.setGoogleId(googleId);
        user.setUserName(generateUniqueUsername(name, email));
        user.setIsGoogleUser(true);
        user.setProfilePictureUrl(profilePictureUrl);
        user.setRole(com.vaiak.moto_compare.security.Role.USER);
        // No password needed for Google users
        return userRepository.save(user);
    }

    private String generateUniqueUsername(String name, String email) {
        // Start with the name, fallback to email prefix
        String baseUsername = name != null ? name.replaceAll("\\s+", "").toLowerCase() :
                              email.substring(0, email.indexOf('@')).toLowerCase();

        String username = baseUsername;
        int counter = 1;

        // Keep trying until we find a unique username
        while (findByUserNameOptional(username).isPresent()) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

}
