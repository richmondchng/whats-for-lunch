package com.richmond.whatsforlunch.session.repository;

import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import com.richmond.whatsforlunch.session.repository.entity.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * Data repository for Session entities.
 */
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    @Query("SELECT s FROM sessions s LEFT JOIN session_participants p ON s.id = p.session.id WHERE (s.owner.id=:userId OR (p.user.id IS NOT NULL AND p.user.id=:userId)) AND s.status IN (:status) GROUP BY s.ID")
    List<SessionEntity> findByUserNameAndStatus(final long userId, final Set<SessionStatus> status);
}
