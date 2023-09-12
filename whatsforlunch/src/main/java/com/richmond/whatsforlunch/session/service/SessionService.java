package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.service.dto.Session;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Service to manage session.
 */
@Service
public class SessionService {

    public Session createNewSession(final LocalDate date, final long owner, final Collection<Long> participants) {
        return null;
    }
}
