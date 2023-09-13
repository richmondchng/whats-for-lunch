package com.richmond.whatsforlunch.session.repository.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test SessionStatus
 */
class SessionStatusTest {

    /**
     * Find valid enum.
     */
    @Test
    void givenNameIsValid_whenFind_returnObject() {
        final SessionStatus result = SessionStatus.find("OPEN");
        assertEquals(SessionStatus.OPEN, result);
    }

    /**
     * Find invalid enum.
     */
    @Test
    void givenNameIsNotValid_whenFind_returnNull() {
        final SessionStatus result = SessionStatus.find("close");
        assertNull(result);
    }
}