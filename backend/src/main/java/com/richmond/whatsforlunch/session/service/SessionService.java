package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.exception.UserNotFoundException;
import com.richmond.whatsforlunch.session.repository.SessionRepository;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantEntity;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantId;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantStatus;
import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import com.richmond.whatsforlunch.session.repository.entity.SessionStatus;
import com.richmond.whatsforlunch.session.service.dto.Session;
import com.richmond.whatsforlunch.session.util.ApplicationMessages;
import com.richmond.whatsforlunch.session.util.ServiceBeanMapper;
import com.richmond.whatsforlunch.users.repository.UserRepository;
import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service to manage session.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    /**
     * Create new session.
     * @param date for date
     * @param ownerId by owner
     * @param participantIds with participants
     * @return newly created session
     */
    public Session createNewSession(final LocalDate date, final long ownerId, final List<Long> participantIds) {

        final List<Long> userIds = new ArrayList<>(participantIds);
        userIds.add(ownerId);

        final Map<Long, UserEntity> userMap = userRepository.findAllById(userIds)
                .stream().collect(Collectors.toMap(UserEntity::getId, Function.identity()));

        final UserEntity owner = userMap.get(ownerId);
        if(owner == null) {
            // owner ID is not valid
            throw new UserNotFoundException(ApplicationMessages.ERROR_OWNER_NOT_FOUND);
        }

        // create a new session
        final SessionEntity session = SessionEntity.builder()
                .date(date).owner(owner).status(SessionStatus.ACTIVE)
                .participants(new ArrayList<>(participantIds.size()))
                .restaurants(Collections.emptyList())
                .selectedRestaurant(0L)
                .build();

        // create participants
        for(Long participantId : participantIds) {
            UserEntity participant = userMap.get(participantId);
            if(participant == null) {
                // participant ID is not valid
                log.error("Unable to find User with Id " + participantId);
                throw new UserNotFoundException(ApplicationMessages.ERROR_PARTICIPANT_NOT_FOUND);
            }
            // add to session
            session.getParticipants().add(ParticipantEntity.builder()
                    .id(new ParticipantId(session.getId(), participant.getId()))
                    .session(session).user(participant).status(ParticipantStatus.PENDING)
                    .build());
        }
        // save
        sessionRepository.saveAndFlush(session);
        return ServiceBeanMapper.mapToBean(session);
    }

    /**
     * Get collection of sessions by owner and status
     * @param ownerId owner ID
     * @param status array of status
     * @return collection of session
     */
    public List<Session> getSessionsByOwner(final long ownerId, final List<String> status) {
        return sessionRepository.findByOwnerAndStatus(ownerId, getStatusObject(status))
                .stream().map(ServiceBeanMapper::mapToBean).toList();
    }

    /**
     * Get collection of sessions by participant and status
     * @param participantId participant ID
     * @param status array of status
     * @return collection of session
     */
    public List<Session> getSessionsByParticipant(final long participantId, final List<String> status) {
        return sessionRepository.findByParticipantAndStatus(participantId, getStatusObject(status))
                .stream().map(ServiceBeanMapper::mapToBean).toList();
    }

    private Set<SessionStatus> getStatusObject(final List<String> status) {
        return status.stream().map(SessionStatus::find).filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Get session by ID.
     * @param id session ID
     * @return session
     */
    public Session getSessionById(final long id) {
        final SessionEntity session = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ApplicationMessages.ERROR_SESSION_ID_INVALID));
        return ServiceBeanMapper.mapToBean(session);
    }

    /**
     * Delete session
     * @param id session ID
     */
    public void deleteSession(final long id) {
        // get session
        final SessionEntity session = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ApplicationMessages.ERROR_SESSION_ID_INVALID));

        // flag session
        session.setStatus(SessionStatus.DELETED);

        // save
        sessionRepository.saveAndFlush(session);
    }


}
