package com.richmond.whatsforlunch.users.repository;

import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User data repository.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Find by username
     * @param userName username
     * @return user entity
     */
    Optional<UserEntity> findByUserName(final String userName);
}
