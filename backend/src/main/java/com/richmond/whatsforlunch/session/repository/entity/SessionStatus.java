package com.richmond.whatsforlunch.session.repository.entity;

import java.util.Arrays;
import java.util.Optional;

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
    CLOSED,
    /**
     * Deleted
     */
    DELETED;

    public String getName() {
        return this.name();
    }

    /**
     * Find mapped Enum object.
     * @param s enum name
     * @return Enum object or null if not found
     */
    public static SessionStatus find(final String s) {
        Optional<SessionStatus> value = Arrays.stream(values()).filter(e -> e.getName().equalsIgnoreCase(s)).findAny();
        return value.isEmpty() ? null : value.get();
    }
}
