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
     * @param userName user name
     * @param restaurant restaurant name
     * @param description description
     */
    public void addRestaurantToSession(final long sessionId, final String userName, final String restaurant, final String description) {
        Assert.isTrue(restaurant.length() < 255, ApplicationMessages.ERROR_RESTAURANT_NAME_OVER_MAX);
        Assert.isTrue(StringUtils.isBlank(description)
                || (StringUtils.isNotBlank(description) && description.length() < 255), ApplicationMessages.ERROR_DESCRIPTION_OVER_MAX);

        // find session
        final SessionEntity session = getActiveSession(sessionId);

        // find user in participants
        final UserEntity user = session.getParticipants().stream()
                .map(p -> p.getUser()).filter(p -> p.getUserName().equals(userName))
                .findAny().orElseThrow(() -> new IllegalArgumentException(ApplicationMessages.ERROR_USER_NOT_PARTICIPANT));

        // add restaurant to session
        final RestaurantEntity restaurantEntity = RestaurantEntity.builder()
                .session(session).addedByUser(user.getId())
                .restaurantName(restaurant).description(description)
                .status(RestaurantStatus.ACTIVE)
                .build();
        session.getRestaurants().add(restaurantEntity);

        // save
        sessionRepository.saveAndFlush(session);
    }

    /**
     * Delete restaurant from session.
     * @param sessionId session ID
     * @param restaurantId restaurant ID
     */
    public void deleteRestaurantFromSession(final long sessionId, final long restaurantId) {
        // find session
        final SessionEntity session = getActiveSession(sessionId);

        // find restaurant in session
        final RestaurantEntity restaurant = session.getRestaurants().stream()
                .filter(r -> r.getId() == restaurantId).findAny()
                .orElseThrow(() -> new IllegalArgumentException(ApplicationMessages.ERROR_RESTAURANT_NOT_IN_SESSION));

        // update status flag
        restaurant.setStatus(RestaurantStatus.DELETED);

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
