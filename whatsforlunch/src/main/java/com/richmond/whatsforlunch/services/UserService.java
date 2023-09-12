package com.richmond.whatsforlunch.services;

import com.richmond.whatsforlunch.respository.UserRepository;
import com.richmond.whatsforlunch.respository.entity.UserEntity;
import com.richmond.whatsforlunch.services.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
    public Collection<User> getAllUsers() {
        return userRepository.findAll()
                .stream().map(this::mapToBean)
                .collect(Collectors.toUnmodifiableList());

    }

    /**
     * Map entity to service bean
     * @param entity UserEntity
     * @return User
     */
    private User mapToBean(final UserEntity entity) {
        return new User(entity.getId(), entity.getUserName(), entity.getFirstName(), entity.getLastName());
    }
}

