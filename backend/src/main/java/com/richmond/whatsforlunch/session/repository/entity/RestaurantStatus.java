package com.richmond.whatsforlunch.session.repository.entity;

/**
 * Status of adding a restaurant.
 */
public enum RestaurantStatus {
    /**
     * Active
     */
    ACTIVE,
    /**
     * Deleted
     */
    DELETED;

    public String getName() {
        return name();
    }
}
