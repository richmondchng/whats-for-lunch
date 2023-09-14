package com.richmond.whatsforlunch.session.service.dto;

/**
 * Service bean representing session participant.
 * @param id user ID
 * @param userName user name
 * @param displayName display name
 * @param status current participant status
 */
public record Participant(long id, String userName, String displayName, String status) { }
