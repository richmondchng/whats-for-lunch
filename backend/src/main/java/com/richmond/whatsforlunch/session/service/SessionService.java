package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.exception.CurrentUserIsNotOwnerException;
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
     * @param ownerUserName by owner username
     * @param participantIds with participants
     * @return newly created session
     */
    public Session createNewSession(final LocalDate date, final String ownerUserName, final List<Long> participantIds) {

        final UserEntity owner = userRepository.findByUserName(ownerUserName)
                .orElseThrow(() -> new UserNotFoundException(ApplicationMessages.ERROR_OWNER_NOT_FOUND));

        final Map<Long, UserEntity> userMap = userRepository.findAllById(participantIds)
                .stream().collect(Collectors.toMap(UserEntity::getId, Function.identity()));

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
     * @param actionedBy username who actioned
     */
    public void deleteSession(final long id, final String actionedBy) {
        // get session
        final SessionEntity session = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ApplicationMessages.ERROR_SESSION_ID_INVALID));
        // check it's the owner
        if(!session.getOwner().getUserName().equals(actionedBy)) {
            throw new CurrentUserIsNotOwnerException();
        }

        // flag session
        session.setStatus(SessionStatus.DELETED);

        // save
        sessionRepository.saveAndFlush(session);
    }

    public List<Session> getSessionsByUser(final String name, final List<String> status) {
        // get user
        final UserEntity user = userRepository.findByUserName(name)
                .orElseThrow(() -> new IllegalArgumentException(ApplicationMessages.ERROR_USER_ID_NOT_FOUND));

        // get sessions by user id
        return sessionRepository.findByUserNameAndStatus(user.getId(), getStatusObject(status)).stream().map(ServiceBeanMapper::mapToBean).toList();
    }
}
