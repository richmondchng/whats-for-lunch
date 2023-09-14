package com.richmond.whatsforlunch.session.controller;

import com.richmond.whatsforlunch.common.controller.StandardResponse;
import com.richmond.whatsforlunch.session.service.ParticipantService;
import com.richmond.whatsforlunch.session.util.ApplicationMessages;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller to manage participant.
 */
@RestController
@RequestMapping("/api/v1/sessions/{sessionId}/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    /**
     * Add participants into session
     * @param sessionId session Id
     * @param body list of participant Ids
     * @return success message
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<ResponseAddParticipants>> addParticipants(@PathVariable final long sessionId,
                                                                                     @RequestBody RequestAddParticipants body) {
        Assert.isTrue(sessionId > 0, ApplicationMessages.ERROR_SESSION_ID_MANDATORY);
        Assert.isTrue( CollectionUtils.isNotEmpty(body.participants()), ApplicationMessages.ERROR_PARTICIPANT_ID_MANDATORY);

        participantService.addParticipantsToSession(sessionId, body.participants());
        return ResponseEntity.ok(new StandardResponse<>(new ResponseAddParticipants(ApplicationMessages.SUCCESS_MESSAGE)));
    }

    /**
     * Delete a participant from session
     * @param sessionId session ID
     * @param participantId participant ID
     * @return action status
     */
    @DeleteMapping(value = "/{participantId}")
    public ResponseEntity<StandardResponse<ResponseDeleteParticipants>> deleteRestaurant(@PathVariable final long sessionId,
                                                                                         @PathVariable final long participantId) {
        Assert.isTrue(sessionId > 0, ApplicationMessages.ERROR_SESSION_ID_MANDATORY);
        Assert.isTrue(participantId > 0, ApplicationMessages.ERROR_PARTICIPANT_ID_MANDATORY);

        participantService.deleteParticipantFromSession(sessionId, participantId);
        return ResponseEntity.ok(new StandardResponse<>(new ResponseDeleteParticipants(ApplicationMessages.SUCCESS_MESSAGE)));
    }
}

record RequestAddParticipants(List<Long> participants) {}

record ResponseAddParticipants(String status) {}

record ResponseDeleteParticipants(String status) {}
