package com.richmond.whatsforlunch.session.service.dto;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Service bean representing a Session.
 * @param id session ID
 * @param date session date
 * @param owner session owner
 * @param participants collection of session participants
 * @param status current session status
 * @param version version
 */
public record Session(long id, LocalDate date, Owner owner, Collection<Participant> participants, String status, long version) { }