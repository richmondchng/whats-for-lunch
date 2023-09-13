package com.richmond.whatsforlunch.session.repository.entity;

/**
 * Session status
 */
public enum SessionStatus {
    /**
     * Open session
     */
    OPEN,
    /**
     * Closed session
     */
    CLOSE,
    /**
     * Deleted
     */
    DELETED;

    public String getName() {
        return this.name();
    }
}
