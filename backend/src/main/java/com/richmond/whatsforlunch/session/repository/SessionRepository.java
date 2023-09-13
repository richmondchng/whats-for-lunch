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

    /**
     * Find sessions by owner, and filtered by status
     * @param ownerId owner ID
     * @param status set of status to filter
     * @return list of sessions
     */
    @Query("SELECT s FROM sessions s WHERE s.owner.id=:ownerId AND s.status IN (:status)")
    List<SessionEntity> findByOwnerAndStatus(final long ownerId, final Set<SessionStatus> status);

    /**
     * Find sessions by participant, and filtered by status
     * @param participantId participant ID
     * @param status set of status to filter
     * @return list of sessions
     */
    @Query("SELECT s FROM sessions s, session_participants p WHERE s.id = p.session.id AND p.user.id=:participantId AND s.status IN (:status)")
    List<SessionEntity> findByParticipantAndStatus(final long participantId, final Set<SessionStatus> status);

}
