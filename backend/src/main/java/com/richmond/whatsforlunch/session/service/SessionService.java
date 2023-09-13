package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.exception.UserNotFoundException;
import com.richmond.whatsforlunch.session.repository.SessionRepository;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantEntity;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantId;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantStatus;
import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import com.richmond.whatsforlunch.session.repository.entity.SessionStatus;
import com.richmond.whatsforlunch.session.service.dto.Owner;
import com.richmond.whatsforlunch.session.service.dto.Participant;
import com.richmond.whatsforlunch.session.service.dto.Session;
import com.richmond.whatsforlunch.users.repository.UserRepository;
import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        final List<Long> userIds = new ArrayList<>();
        userIds.addAll(participantIds);
        userIds.add(ownerId);

        final Map<Long, UserEntity> userMap = userRepository.findAllById(userIds)
                .stream().collect(Collectors.toMap(UserEntity::getId, Function.identity()));

        final UserEntity owner = userMap.get(ownerId);
        if(owner == null) {
            // owner ID is not valid
            throw new UserNotFoundException("Owner is not found");
        }

        // create a new session
        final SessionEntity session = SessionEntity.builder()
                .date(date).owner(owner).status(SessionStatus.OPEN)
                .participants(new ArrayList<>(participantIds.size()))
                .build();

        // create participants
        for(Long participantId : participantIds) {
            UserEntity participant = userMap.get(participantId);
            if(participant == null) {
                // participant ID is not valid
                log.error("Unable to find User with Id " + participantId);
                throw new UserNotFoundException("Participant is not found");
            }
            // add to session
            session.getParticipants().add(ParticipantEntity.builder()
                    .id(new ParticipantId(session.getId(), participant.getId()))
                    .session(session).user(participant).status(ParticipantStatus.PENDING)
                    .build());
        }
        // save
        sessionRepository.saveAndFlush(session);
        return mapToBean(session);
    }


    private Session mapToBean(final SessionEntity entity) {
        return new Session(entity.getId(), entity.getDate(), mapToBean(entity.getOwner()),
                entity.getParticipants().stream().map(this::mapToBean).collect(Collectors.toUnmodifiableList()),
                entity.getStatus().getName(), entity.getVersion());
    }

    private Owner mapToBean(final UserEntity entity) {
        return new Owner(entity.getId(), entity.getUserName(), entity.getFirstName());
    }
    private Participant mapToBean(final ParticipantEntity entity) {
        return new Participant(entity.getUser().getId(), entity.getUser().getUserName(), entity.getUser().getFirstName(),
                entity.getStatus().getName());
    }

    /**
     * Get collection of sessions by owner and status
     * @param ownerId owner ID
     * @param status array of status
     * @return collection of session
     */
    public List<Session> getSessionsByOwner(final long ownerId, final List<String> status) {
        return sessionRepository.findByOwnerAndStatus(ownerId, getStatusObject(status))
                .stream().map(this::mapToBean).collect(Collectors.toUnmodifiableList());
    }

    /**
     * Get collection of sessions by participant and status
     * @param participantId participant ID
     * @param status array of status
     * @return collection of session
     */
    public List<Session> getSessionsByParticipant(final long participantId, final List<String> status) {
        return sessionRepository.findByParticipantAndStatus(participantId, getStatusObject(status))
                .stream().map(this::mapToBean).collect(Collectors.toUnmodifiableList());
    }

    private Set<SessionStatus> getStatusObject(final List<String> status) {
        return status.stream().map(s -> SessionStatus.find(s)).filter(s -> s != null)
                .collect(Collectors.toUnmodifiableSet());
    }
}
