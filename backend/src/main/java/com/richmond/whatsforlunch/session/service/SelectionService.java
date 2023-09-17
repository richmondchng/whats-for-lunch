package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.exception.CurrentUserIsNotOwnerException;
import com.richmond.whatsforlunch.session.exception.NoRestaurantInSessionException;
import com.richmond.whatsforlunch.session.repository.SessionRepository;
import com.richmond.whatsforlunch.session.repository.entity.RestaurantStatus;
import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import com.richmond.whatsforlunch.session.repository.entity.SessionStatus;
import com.richmond.whatsforlunch.session.service.dto.Restaurant;
import com.richmond.whatsforlunch.session.service.strategy.RestaurantSelectionStrategy;
import com.richmond.whatsforlunch.session.service.strategy.RestaurantSelectionStrategyFactory;
import com.richmond.whatsforlunch.session.util.ApplicationMessages;
import com.richmond.whatsforlunch.session.util.ServiceBeanMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to select restaurant from session.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SelectionService {

    private final SessionRepository sessionRepository;
    private final RestaurantSelectionStrategyFactory restaurantSelectionStrategyFactory;

    /**
     * Select a restaurant from the session, then close the session
     * @param sessionId session ID
     * @param actionedBy actioned by username
     * @param strategy selection strategy
     * @return updated Session
     */
    public Restaurant selectRestaurant(final long sessionId, final String actionedBy, final String strategy) {
        // get session
        final SessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException(ApplicationMessages.ERROR_SESSION_ID_INVALID));
        if(SessionStatus.ACTIVE != session.getStatus()) {
            // session is not opened
            throw new IllegalArgumentException(ApplicationMessages.ERROR_SESSION_NOT_OPENED);
        }
        // check it's the owner
        if(!session.getOwner().getUserName().equals(actionedBy)) {
            throw new CurrentUserIsNotOwnerException();
        }
        // check at least 1 restaurant in session
        if(session.getRestaurants().size() == 0) {
            throw new NoRestaurantInSessionException();
        }

        // pick strategy
        final RestaurantSelectionStrategy selector = restaurantSelectionStrategyFactory.getStrategy(strategy);

        // use strategy to select restaurant
        final List<Restaurant> restaurantList = session.getRestaurants().stream()
                .filter(r -> RestaurantStatus.ACTIVE == r.getStatus())
                .map(ServiceBeanMapper::mapToBean).toList();
        final Restaurant selectedRestaurant = selector.pickRestaurant(restaurantList);

        // update session
        session.setSelectedRestaurant(selectedRestaurant.id());
        session.setStatus(SessionStatus.CLOSED);

        // save
        sessionRepository.saveAndFlush(session);

        // return
        return selectedRestaurant;
    }
}
