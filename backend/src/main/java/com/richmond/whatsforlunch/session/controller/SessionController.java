package com.richmond.whatsforlunch.session.controller;

import com.richmond.whatsforlunch.common.controller.StandardResponse;
import com.richmond.whatsforlunch.session.controller.dto.ResponseSession;
import com.richmond.whatsforlunch.session.controller.dto.ResponseSessionUtil;
import com.richmond.whatsforlunch.session.service.SessionService;
import com.richmond.whatsforlunch.session.service.dto.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller to manage session
 */
@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    private static final String ERROR_SESSION_ID_MANDATORY = "Session ID is mandatory";
    private static final String ERROR_SESSION_DATE_MANDATORY = "Date is mandatory";
    private static final String ERROR_SESSION_OWNER_ID_MANDATORY = "Owner Id is mandatory";

    /**
     * POST action to create a new session
     * @return newly create session
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<ResponseSession>> createNewSession(
            @RequestBody final RequestCreateNewSession request) {

        Assert.notNull(request.date(), ERROR_SESSION_DATE_MANDATORY);
        Assert.isTrue(request.owner() > 0, ERROR_SESSION_OWNER_ID_MANDATORY);

        final Session session = sessionService.createNewSession(request.date(), request.owner(), request.participants());
        // return created session
        return ResponseEntity.ok(new StandardResponse<>(ResponseSessionUtil.mapToBean(session)));
    }


    /**
     * Get sessions.
     * @param status list of status to returned
     * @param owner filtered by owner ID
     * @param participant filtered by participant ID
     * @return list of sessions
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<ResponseSession>> getSessions(
            @RequestParam(defaultValue = "OPEN,CLOSED") final List<String> status,
            @RequestParam(required = false) final Long owner,
            @RequestParam(required = false) final Long participant) {

        final List<Session> results;
        if(owner != null) {
            results = sessionService.getSessionsByOwner(owner, status);
        } else if(participant != null) {
            results = sessionService.getSessionsByParticipant(participant, status);
        } else {
            results = new ArrayList<>(0);
        }
        return ResponseEntity.ok(new StandardResponse<>(ResponseSessionUtil.mapToBeans(results)));
    }

    /**
     * Get session by ID.
     * @param id session ID
     * @return list of sessions
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<ResponseSession>> getSessionById(@PathVariable final long id) {
        Assert.isTrue(id > 0, ERROR_SESSION_ID_MANDATORY);
        final Session session = sessionService.getSessionById(id);
        return ResponseEntity.ok(new StandardResponse<>(ResponseSessionUtil.mapToBean(session)));
    }
}

/**
 * Request body for creating new session
 * @param date session date
 * @param owner session owner id
 * @param participants collection of participants' Ids
 */
record RequestCreateNewSession(LocalDate date, long owner, List<Long> participants) {}


