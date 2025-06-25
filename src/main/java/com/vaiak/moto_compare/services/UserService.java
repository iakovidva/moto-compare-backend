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

    public void saveUser(User user) {
        userRepository.save(user);
    }

}

