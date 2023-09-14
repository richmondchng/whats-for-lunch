package com.richmond.whatsforlunch.session.service.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Service bean representing a Session.
 * @param id session ID
 * @param date session date
 * @param owner session owner
 * @param participants list of session participants
 * @param restaurants list of session restaurants
 * @param selectedRestaurant selected restaurant ID
 * @param status current session status
 * @param version version
 */
public record Session(long id, LocalDate date, Owner owner, List<Participant> participants, List<Restaurant> restaurants,
                      long selectedRestaurant, String status, long version) { }