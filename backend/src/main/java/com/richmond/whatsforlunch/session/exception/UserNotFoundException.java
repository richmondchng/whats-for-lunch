package com.richmond.whatsforlunch.session.exception;

/**
 * Custom exception for User not found.
 */
public class UserNotFoundException extends IllegalArgumentException {

    public UserNotFoundException(final String message) {
        super(message);
    }
}
