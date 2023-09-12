package com.richmond.whatsforlunch.respository;

import com.richmond.whatsforlunch.respository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User data repository.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
