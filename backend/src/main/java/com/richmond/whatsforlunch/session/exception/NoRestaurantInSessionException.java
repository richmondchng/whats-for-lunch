package com.richmond.whatsforlunch.session.exception;

/**
 * Exception when there are no restaurant in session
 */
public class NoRestaurantInSessionException extends RuntimeException {
    public NoRestaurantInSessionException() {
        super("No restaurant in session, please add one restaurant to continue");
    }
}
