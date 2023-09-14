package com.richmond.whatsforlunch.session.service.dto;

/**
 * Service record describing restaurant.
 * @param id record Id
 * @param userId added by user
 * @param restaurant restaurant name
 * @param description description
 */
public record Restaurant (long id, long userId, String restaurant, String description) {
}
