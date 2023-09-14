package com.richmond.whatsforlunch.session.controller.dto;

import com.richmond.whatsforlunch.session.service.dto.Owner;
import com.richmond.whatsforlunch.session.service.dto.Participant;
import com.richmond.whatsforlunch.session.service.dto.Restaurant;
import com.richmond.whatsforlunch.session.service.dto.Session;

import java.util.List;

/**
 * Util class to map beans.
 */
public final class ResponseSessionUtil {
    private ResponseSessionUtil() {}

    /**
     * Map to response records.
     * @param sessions service beans
     * @return list of response records
     */
    public static List<ResponseSession> mapToBeans(final List<Session> sessions) {
        return sessions.stream().map(ResponseSessionUtil::mapToBean).toList();
    }

    /**
     * Map to response record
     * @param session service bean
     * @return response record
     */
    public static ResponseSession mapToBean(final Session session) {
        return new ResponseSession(session.id(), session.date(),
                mapToBean(session.owner()),
                session.participants().stream().map(ResponseSessionUtil::mapToBean).toList(),
                session.restaurants().stream().map(ResponseSessionUtil::mapToBean).toList(),
                session.status());
    }

    private static ResponseSessionOwner mapToBean(final Owner bean) {
        return new ResponseSessionOwner(bean.id(), bean.userName(), bean.displayName());
    }

    private static ResponseSessionParticipant mapToBean(final Participant bean) {
        return new ResponseSessionParticipant(bean.id(), bean.userName(), bean.displayName(), bean.status());
    }

    private static ResponseSessionRestaurant mapToBean(final Restaurant bean) {
        return new ResponseSessionRestaurant(bean.id(), bean.restaurant(), bean.description(), bean.userId(), bean.status());
    }
}
