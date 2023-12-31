package com.richmond.whatsforlunch.session.controller;

import com.richmond.whatsforlunch.common.controller.StandardResponse;
import com.richmond.whatsforlunch.session.controller.dto.ResponseSession;
import com.richmond.whatsforlunch.session.controller.dto.ResponseSessionUtil;
import com.richmond.whatsforlunch.session.service.SelectionService;
import com.richmond.whatsforlunch.session.service.SessionService;
import com.richmond.whatsforlunch.session.service.dto.Restaurant;
import com.richmond.whatsforlunch.session.service.dto.Session;
import com.richmond.whatsforlunch.session.util.ApplicationMessages;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller to manage session
 */
@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final SelectionService selectionService;

    /**
     * POST action to create a new session
     * @return newly create session
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<ResponseSession>> createNewSession(
            @RequestBody final RequestCreateNewSession request, final Principal principal) {

        Assert.notNull(request.date(), ApplicationMessages.ERROR_SESSION_DATE_MANDATORY);

        final Session session = sessionService.createNewSession(request.date(), principal.getName(),
                request.participants());
        // return created session
        return ResponseEntity.ok(new StandardResponse<>(ResponseSessionUtil.mapToBean(session)));
    }


    /**
     * Get sessions.
     * @param status list of status to returned
     * @param principal user
     * @return list of sessions
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<ResponseSession>> getSessions(
            @RequestParam(defaultValue = "ACTIVE,CLOSED") final List<String> status,
            final Principal principal) {

        final List<Session> results = sessionService.getSessionsByUser(principal.getName(), status);
        return ResponseEntity.ok(new StandardResponse<>(ResponseSessionUtil.mapToBeans(results)));
    }

    /**
     * Get session by ID.
     * @param id session ID
     * @return list of sessions
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<ResponseSession>> getSessionById(@PathVariable final long id) {
        Assert.isTrue(id > 0, ApplicationMessages.ERROR_SESSION_ID_MANDATORY);
        final Session session = sessionService.getSessionById(id);
        return ResponseEntity.ok(new StandardResponse<>(ResponseSessionUtil.mapToBean(session)));
    }

    /**
     * Delete session.
     * @param id session ID
     * @return action status
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<StandardResponse<ResponseDeleteSession>> deleteSession(@PathVariable final long id, final Principal principal) {
        Assert.isTrue(id > 0, ApplicationMessages.ERROR_SESSION_ID_MANDATORY);
        sessionService.deleteSession(id, principal.getName());
        return ResponseEntity.ok(new StandardResponse<>(new ResponseDeleteSession(id, "DELETE",
                ApplicationMessages.SUCCESS_MESSAGE)));
    }

    /**
     * Patch session - close and select a restaurant.
     * @param id session Id
     * @param body selection properties
     * @return ResponseSession
     */
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<ResponsePatchSession>> patchSession(
            @PathVariable final long id, @RequestBody final RequestPatchSession body, final Principal principal) {
        final Restaurant restaurant = selectionService.selectRestaurant(id, principal.getName(),
                // default to random
                StringUtils.isBlank(body.strategy()) ? "RANDOM" : body.strategy());

        return ResponseEntity.ok(new StandardResponse<>(new ResponsePatchSession(id, restaurant.id(),
                restaurant.restaurant())));
    }
}

/**
 * Request body for creating new session
 * @param date session date
 * @param participants collection of participants' Ids
 */
record RequestCreateNewSession(LocalDate date, List<Long> participants) {}

/**
 * Request to patch (i.e., to select a restaurant)
 * @param strategy selection strategy
 */
record RequestPatchSession(String strategy) {}

/**
 * Response after patch (i.e., restaurant selected)
 * @param sessionId session ID
 * @param restaurantId restaurant ID
 * @param restaurantName restaurant Name
 */
record ResponsePatchSession(long sessionId, long restaurantId, String restaurantName) {}

/**
 * Response when deleting a session
 * @param sessionId session ID
 * @param action action performed
 * @param status status
 */
record ResponseDeleteSession(long sessionId, String action, String status) {}



