package com.richmond.whatsforlunch.session.controller;

import com.richmond.whatsforlunch.session.service.ParticipantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test ParticipantController.
 */
@WebMvcTest(controllers = { ParticipantController.class })
class ParticipantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParticipantService participantService;

    /**
     * Given request body does not contain mandatory participants field, when invoke POST /api/v1/sessions/{sessionId}/participants, fail and throw error
     * @throws Exception exception
     */
    @Test
    void givenRequestBodyWithoutParticipants_whenAddParticipants_returnError() throws Exception {

        final String content = "{\"participants\": []}";
        mockMvc.perform(post("/api/v1/sessions/2/participants").contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Participant ID is mandatory")))
                .andExpect(jsonPath("$.path", is("/api/v1/sessions/2/participants")));

        verifyNoInteractions(participantService);
    }

    /**
     * Given path session Id is zero, when invoke POST /api/v1/sessions/{sessionId}/participants, fail and throw error
     * @throws Exception exception
     */
    @Test
    void givenSessionIdIsZero_whenAddParticipants_returnError() throws Exception {

        final String content = "{\"participants\": [6, 7]}";
        mockMvc.perform(post("/api/v1/sessions/0/participants").contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Session ID is mandatory")))
                .andExpect(jsonPath("$.path", is("/api/v1/sessions/0/participants")));

        verifyNoInteractions(participantService);
    }

    /**
     * Given request body is valid, when invoke DELETE /api/v1/sessions/{sessionId}/participants, return success status
     * @throws Exception exception
     */
    @Test
    void givenValidRequest_whenAddParticipants_returnSuccess() throws Exception {

        final String content = "{\"participants\": [6, 7]}";
        mockMvc.perform(post("/api/v1/sessions/2/participants")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].status", is("Success")));

        verify(participantService, times(1)).addParticipantsToSession(eq(2L), eq(List.of(6L, 7L)));
    }

    /**
     * Given request is valid, when invoke DELETE /api/v1/sessions/{sessionId}/participants/{participantId}, return success status
     * @throws Exception exception
     */
    @Test
    void givenRequestBodyIsValid_whenDeleteParticipant_returnSuccessStatus() throws Exception {

        mockMvc.perform(delete("/api/v1/sessions/2/participants/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].status", is("Success")));

        verify(participantService, times(1)).deleteParticipantFromSession(2L, 5L);
    }

    /**
     * Given path session ID is zero, when invoke DELETE /api/v1/sessions/{sessionId}/participants/{participantId}, fail and throw error
     * @throws Exception exception
     */
    @Test
    void givenSessionIdIsZero_whenDeleteParticipant_returnError() throws Exception {

        mockMvc.perform(delete("/api/v1/sessions/0/participants/5"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Session ID is mandatory")))
                .andExpect(jsonPath("$.path", is("/api/v1/sessions/0/participants/5")));

        verifyNoInteractions(participantService);
    }

    /**
     * Given path restaurant ID is zero, when invoke DELETE /api/v1/sessions/{sessionId}/participants/{participantId}, fail and throw error
     * @throws Exception exception
     */
    @Test
    void givenParticipantIdIsZero_whenDeleteParticipant_returnError() throws Exception {

        mockMvc.perform(delete("/api/v1/sessions/2/participants/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Participant ID is mandatory")))
                .andExpect(jsonPath("$.path", is("/api/v1/sessions/2/participants/0")));

        verifyNoInteractions(participantService);
    }
}