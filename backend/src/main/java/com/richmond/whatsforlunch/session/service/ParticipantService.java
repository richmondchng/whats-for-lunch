package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final SessionRepository sessionRepository;

    /**
     * Add participants to Session.
     * @param sessionId session Id
     * @param participants participants
     */
    public void addParticipantsToSession(final long sessionId, final List<Long> participants) {
        // get session

        // if participant is not already in session, add

        // save
    }
}
