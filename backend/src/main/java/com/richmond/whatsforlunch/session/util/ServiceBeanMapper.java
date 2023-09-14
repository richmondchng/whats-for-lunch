package com.richmond.whatsforlunch.session.util;

import com.richmond.whatsforlunch.session.repository.entity.ParticipantEntity;
import com.richmond.whatsforlunch.session.repository.entity.RestaurantEntity;
import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import com.richmond.whatsforlunch.session.service.dto.Owner;
import com.richmond.whatsforlunch.session.service.dto.Participant;
import com.richmond.whatsforlunch.session.service.dto.Restaurant;
import com.richmond.whatsforlunch.session.service.dto.Session;
import com.richmond.whatsforlunch.users.repository.entity.UserEntity;

/**
 * Util class for mapping entity to service bean
 */
public final class ServiceBeanMapper {

    private ServiceBeanMapper() {}

    /**
     * Map SessionEntity to Service bean
     * @param entity SessionEntity
     * @return Session
     */
    public static Session mapToBean(final SessionEntity entity) {
        return new Session(entity.getId(), entity.getDate(), mapToOwnerBean(entity.getOwner()),
                entity.getParticipants().stream().map(ServiceBeanMapper::mapToBean).toList(),
                entity.getRestaurants().stream().map(ServiceBeanMapper::mapToBean).toList(),
                entity.getSelectedRestaurant(), entity.getStatus().getName(), entity.getVersion());
    }

    /**
     * Map UserEntity to Service bean
     * @param entity UserEntity
     * @return Owner
     */
    private static Owner mapToOwnerBean(final UserEntity entity) {
        return new Owner(entity.getId(), entity.getUserName(), entity.getFirstName());
    }

    /**
     * Map ParticipantEntity to Service bean
     * @param entity ParticipantEntity
     * @return Participant
     */
    private static Participant mapToBean(final ParticipantEntity entity) {
        return new Participant(entity.getUser().getId(), entity.getUser().getUserName(), entity.getUser().getFirstName(),
                entity.getStatus().getName());
    }

    /**
     * Map RestaurantEntity to Service bean
     * @param entity RestaurantEntity
     * @return Restaurant
     */
    public static Restaurant mapToBean(final RestaurantEntity entity) {
        return new Restaurant(entity.getId(), entity.getAddedByUser(), entity.getRestaurantName(),
                entity.getDescription(), entity.getStatus().getName());
    }
}
