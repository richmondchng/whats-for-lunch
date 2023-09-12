package com.richmond.whatsforlunch.users.repository;

import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User data repository.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
