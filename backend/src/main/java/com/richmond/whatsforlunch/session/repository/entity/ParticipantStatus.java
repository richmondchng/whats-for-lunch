package com.richmond.whatsforlunch.session.repository.entity;

/**
 * Enum of participant status.
 */
public enum ParticipantStatus {
    /**
     * Initial state, pending invite
     */
    PENDING,
    /**
     * Invited to join
     */
    INVITED,
    /**
     * Has joined session
     */
    JOINED,
    /**
     * Exit session
     */
    EXIT;

    public String getName() {
        return this.name();
    }
}
