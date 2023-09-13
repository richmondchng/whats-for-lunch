package com.richmond.whatsforlunch.session.repository;

import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
}
