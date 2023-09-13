package com.richmond.whatsforlunch.session.controller;

import com.richmond.whatsforlunch.common.controller.StandardResponse;
import com.richmond.whatsforlunch.session.service.SessionService;
import com.richmond.whatsforlunch.session.service.dto.Owner;
import com.richmond.whatsforlunch.session.service.dto.Participant;
import com.richmond.whatsforlunch.session.service.dto.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * POST action to create a new session
     * @return newly create session
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<ResponseSession>> createNewSession(
            @RequestBody final RequestCreateNewSession request) {

        Assert.notNull(request.date(), "Date is mandatory");
        Assert.isTrue(request.owner() > 0, "Owner Id is mandatory");

        final Session session = sessionService.createNewSession(request.date(), request.owner(), request.participants());
        // return created session
        return ResponseEntity.ok(new StandardResponse<>(mapToBean(session)));
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
        return ResponseEntity.ok(new StandardResponse<>(mapToBean(results)));
    }


    private List<ResponseSession> mapToBean(final List<Session> sessions) {
        return sessions.stream().map(this::mapToBean).toList();
    }

    private ResponseSession mapToBean(final Session session) {
        return new ResponseSession(session.id(), session.date(),
                mapToBean(session.owner()),
                session.participants().stream().map(this::mapToBean).toList(),
                session.status());
    }

    private ResponseSessionOwner mapToBean(final Owner bean) {
        return new ResponseSessionOwner(bean.id(), bean.userName(), bean.displayName());
    }

    private ResponseSessionParticipant mapToBean(final Participant bean) {
        return new ResponseSessionParticipant(bean.id(), bean.userName(), bean.displayName(), bean.status());
    }
}

/**
 * Request body for creating new session
 * @param date session date
 * @param owner session owner id
 * @param participants collection of participants' Ids
 */
record RequestCreateNewSession(LocalDate date, long owner, List<Long> participants) {}

/**
 * Response body when new session is created.
 * @param id session id
 * @param date session date
 * @param owner session owner id
 * @param participants collection of participants' Ids
 */
record ResponseSession(long id, LocalDate date, ResponseSessionOwner owner, List<ResponseSessionParticipant> participants, String status) {}

record ResponseSessionOwner(long id, String userName, String displayName) {}

record ResponseSessionParticipant(long id, String userName, String displayName, String status) {}

