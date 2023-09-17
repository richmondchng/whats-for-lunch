package com.richmond.whatsforlunch.session.exception;

import com.richmond.whatsforlunch.session.util.ApplicationMessages;

/**
 * Exception when current user is not the owner of session.
 */
public class CurrentUserIsNotOwnerException extends RuntimeException {
    public CurrentUserIsNotOwnerException() {
        super(ApplicationMessages.ERROR_SESSION_OWNER_IS_NOT_CURRENT_USER);
    }
}
