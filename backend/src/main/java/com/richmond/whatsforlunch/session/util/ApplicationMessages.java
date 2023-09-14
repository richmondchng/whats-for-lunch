package com.richmond.whatsforlunch.session.util;

/**
 * Util class to contain application messages.
 */
public final class ApplicationMessages {

    public static final String ERROR_SESSION_ID_MANDATORY = "Session ID is mandatory";
    public static final String ERROR_SESSION_DATE_MANDATORY = "Date is mandatory";
    public static final String ERROR_SESSION_OWNER_ID_MANDATORY = "Owner Id is mandatory";
    public static final String ERROR_USER_ID_MANDATORY = "User ID is mandatory";
    public static final String ERROR_RESTAURANT_MANDATORY = "Restaurant is mandatory";
    public static final String ERROR_RESTAURANT_ID_MANDATORY = "Restaurant ID is mandatory";
    public static final String ERROR_PARTICIPANT_ID_MANDATORY = "Participant ID is mandatory";
    public static final String ERROR_PATCH_SESSION_STATUS_MANDATORY = "Patch session status is mandatory";
    public static final String SUCCESS_MESSAGE = "Success";

    public static final String ERROR_OWNER_NOT_FOUND = "Owner is not found";
    public static final String ERROR_PARTICIPANT_NOT_FOUND = "Participant is not found";
    public static final String ERROR_SESSION_ID_INVALID = "Session ID is invalid";
    public static final String ERROR_SESSION_NOT_OPENED = "Session is not active";
    public static final String ERROR_USER_NOT_PARTICIPANT = "User is not a participant";
    public static final String ERROR_RESTAURANT_NAME_OVER_MAX = "Restaurant name is too long (max 255 character)";
    public static final String ERROR_DESCRIPTION_OVER_MAX = "Description is too long (max 255 character)";

    public static final String ERROR_RESTAURANT_NOT_IN_SESSION = "Restaurant is not session";
    public static final String ERROR_PARTICIPANT_NOT_IN_SESSION = "Participant is not session";

    public static final String ERROR_STRATEGY_NOT_FOUND = "Strategy is not found";

    private ApplicationMessages() {}
}
