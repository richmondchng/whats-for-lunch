package com.richmond.whatsforlunch.session.controller.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Record describing a session.
 * @param id session ID
 * @param date session date
 * @param owner session owner
 * @param participants session participants
 * @param restaurants session restaurants
 * @param status session current status
 */
public record ResponseSession(long id, LocalDate date, ResponseSessionOwner owner,
                              List<ResponseSessionParticipant> participants,
                              List<ResponseSessionRestaurant> restaurants,
                              String status) {}

/**
 * Record describing session owner.
 * @param id owner ID
 * @param userName owner username
 * @param displayName owner display name
 */
record ResponseSessionOwner(long id, String userName, String displayName) {}

/**
 * Record describing session participant.
 * @param id participant ID
 * @param userName participant username
 * @param displayName participant display name
 * @param status participant current status
 */
record ResponseSessionParticipant(long id, String userName, String displayName, String status) {}

/**
 * Record describing session restaurant.
 * @param id record id
 * @param restaurantName restaurant name
 * @param description description
 * @param addedBy added by this user ID
 */
record ResponseSessionRestaurant(long id, String restaurantName, String description, long addedBy) {}
