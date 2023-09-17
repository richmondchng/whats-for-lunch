package com.richmond.whatsforlunch.users.service;

import com.richmond.whatsforlunch.users.repository.UserRepository;
import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import com.richmond.whatsforlunch.users.service.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User service
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Get all users.
     * @return collection of users, or empty collection
     */
    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream().map(this::mapToBean)
                .collect(Collectors.toUnmodifiableList());

    }

    /**
     * Get user by username
     * @param userName user name
     * @return User
     */
    public User getUserByUserName(final String userName) {
        return userRepository.findByUserName(userName)
                .map(this::mapToBean)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));
    }

    /**
     * Map entity to service bean
     * @param entity UserEntity
     * @return User
     */
    private User mapToBean(final UserEntity entity) {
        return new User(entity.getId(), entity.getUserName(), null, null,
                entity.getFirstName(), entity.getLastName());
    }
}

