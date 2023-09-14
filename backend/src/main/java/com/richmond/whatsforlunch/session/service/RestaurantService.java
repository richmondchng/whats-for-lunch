package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.repository.SessionRepository;
import com.richmond.whatsforlunch.session.repository.entity.RestaurantEntity;
import com.richmond.whatsforlunch.session.repository.entity.RestaurantStatus;
import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import com.richmond.whatsforlunch.session.repository.entity.SessionStatus;
import com.richmond.whatsforlunch.session.util.ApplicationMessages;
import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Service to manage restaurants in session.
 */
@Service
@RequiredArgsConstructor
@Transactional
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
        Assert.isTrue(restaurant.length() < 255, ApplicationMessages.ERROR_RESTAURANT_NAME_OVER_MAX);
        Assert.isTrue(StringUtils.isBlank(description)
                || (StringUtils.isNotBlank(description) && description.length() < 255), ApplicationMessages.ERROR_DESCRIPTION_OVER_MAX);

        // find session
        final SessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException(ApplicationMessages.ERROR_SESSION_ID_INVALID));
        if(SessionStatus.ACTIVE != session.getStatus()) {
            // session is not opened
            throw new IllegalArgumentException(ApplicationMessages.ERROR_SESSION_NOT_OPENED);
        }

        // find user in participants
        final UserEntity user = session.getParticipants().stream()
                .map(p -> p.getUser()).filter(p -> p.getId() == userId)
                .findAny().orElseThrow(() -> new IllegalArgumentException(ApplicationMessages.ERROR_USER_NOT_PARTICIPANT));

        // add restaurant to session
        final RestaurantEntity restaurantEntity = RestaurantEntity.builder()
                .session(session).addedByUser(userId)
                .restaurantName(restaurant).description(description)
                .status(RestaurantStatus.ACTIVE)
                .build();
        session.getRestaurants().add(restaurantEntity);

        // save
        sessionRepository.saveAndFlush(session);
    }
}
