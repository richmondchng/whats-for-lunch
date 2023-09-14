package com.richmond.whatsforlunch.session.service.dto;

/**
 * Service bean representing session owner
 * @param id user ID
 * @param userName user name
 * @param displayName user display name
 */
public record Owner(long id, String userName, String displayName) { }
