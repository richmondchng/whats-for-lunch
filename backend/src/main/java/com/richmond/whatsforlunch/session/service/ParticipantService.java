package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.repository.SessionRepository;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantEntity;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantId;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantStatus;
import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import com.richmond.whatsforlunch.session.repository.entity.SessionStatus;
import com.richmond.whatsforlunch.session.util.ApplicationMessages;
import com.richmond.whatsforlunch.users.repository.UserRepository;
import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service managing participants
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ParticipantService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    /**
     * Add participants to Session.
     * @param sessionId session Id
     * @param participants participants
     */
    public void addParticipantsToSession(final long sessionId, final List<Long> participants) {
        // get session
        final SessionEntity session = getActiveSession(sessionId);

        // if participant is not already in session, add
        final Set<Long> alreadyInSession = session.getParticipants().stream()
                .map(p -> p.getId().getUserId())
                .collect(Collectors.toUnmodifiableSet());
        final List<Long> notInSession = participants.stream()
                .filter(id -> !alreadyInSession.contains(id))
                .collect(Collectors.toUnmodifiableList());

        final List<UserEntity> users = userRepository.findAllById(notInSession);
        for (UserEntity user : users) {
            final ParticipantEntity participant = ParticipantEntity.builder()
                    .id(new ParticipantId(session.getId(), user.getId()))
                    .session(session).user(user).status(ParticipantStatus.PENDING)
                    .build();
            session.getParticipants().add(participant);
        }
        // save
        sessionRepository.saveAndFlush(session);
    }

    /**
     * Delete participant from session
     * @param sessionId session ID
     * @param participantId participant ID
     */
    public void deleteParticipantFromSession(final long sessionId, final long participantId) {
        // get session
        final SessionEntity session = getActiveSession(sessionId);

        // determine if participant in session
        final ParticipantEntity participant = session.getParticipants().stream()
                .filter(p -> p.getId().getUserId() == participantId).findAny()
                .orElseThrow(() -> new IllegalArgumentException(ApplicationMessages.ERROR_PARTICIPANT_NOT_IN_SESSION));

        // flag participant as deleted
        participant.setStatus(ParticipantStatus.DELETED);

        // save
        sessionRepository.saveAndFlush(session);
    }

    private SessionEntity getActiveSession(final long sessionId) {
        final SessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException(ApplicationMessages.ERROR_SESSION_ID_INVALID));
        if(SessionStatus.ACTIVE != session.getStatus()) {
            // session is not opened
            throw new IllegalArgumentException(ApplicationMessages.ERROR_SESSION_NOT_OPENED);
        }
        return session;
    }
}
