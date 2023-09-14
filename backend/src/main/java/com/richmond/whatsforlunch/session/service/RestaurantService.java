package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service to manage restaurants in session.
 */
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final SessionRepository sessionRepository;

    /**
     * Add restaurant to session
     * @param sessionId session ID
     * @param userId user ID
     * @param restaurant restaurant name
     * @param description description
     */
    public void addRestaurantToSession(final long sessionId, final long userId, final String restaurant, final String description) {
        // find session

        // find user in participants

        // add restaurant to session

        // save
    }
}
