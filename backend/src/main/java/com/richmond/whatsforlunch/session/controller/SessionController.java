package com.richmond.whatsforlunch.session.controller;

import com.richmond.whatsforlunch.common.controller.StandardResponse;
import com.richmond.whatsforlunch.session.service.SessionService;
import com.richmond.whatsforlunch.session.service.dto.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;
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
     * @return
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<ResponseCreateNewSession>> createNewSession(
            @RequestBody final RequestCreateNewSession request) {

        Assert.notNull(request.date(), "Date is mandatory");
        Assert.isTrue(request.owner() > 0, "Owner Id is mandatory");

        final Session session = sessionService.createNewSession(request.date(), request.owner(), request.participants());
        // return created session
        return ResponseEntity.ok(new StandardResponse<>(mapToCreateNewSessionBean(session)));
    }

    private ResponseCreateNewSession mapToCreateNewSessionBean(final Session session) {
        return new ResponseCreateNewSession(session.id(), session.date(), session.owner(),
                List.copyOf(session.participants()));
    }
}

/**
 * Request body for creating new session
 * @param date session date
 * @param owner session owner id
 * @param participants collection of participants' Ids
 */
record RequestCreateNewSession(LocalDate date, long owner, Collection<Long> participants) {}

/**
 * Response body when new session is created.
 * @param id session id
 * @param date session date
 * @param owner session owner id
 * @param participants collection of participants' Ids
 */
record ResponseCreateNewSession(long id, LocalDate date, long owner, Collection<Long> participants) {}