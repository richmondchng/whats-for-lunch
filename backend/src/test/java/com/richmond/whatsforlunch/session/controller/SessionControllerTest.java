package com.richmond.whatsforlunch.session.controller;

import com.richmond.whatsforlunch.session.service.SessionService;
import com.richmond.whatsforlunch.session.service.dto.Session;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test SessionController
 */
@WebMvcTest(controllers = { SessionController.class })
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @Captor
    private ArgumentCaptor<Collection<Long>> participantsCaptor;

    /**
     * Given request body is valid, when invoke POST /api/v1/sessions, return created session
     * @throws Exception exception
     */
    @Test
    void givenRequestBodyIsValid_whenCreateNewSession_returnNewSession() throws Exception {

        when(sessionService.createNewSession(any(LocalDate.class), anyLong(), anyCollection())).thenReturn(
          new Session(99L, LocalDate.of(2023, 9, 12), 2L, List.of(2L, 5L, 6L, 7L))
        );

        final String content = "{\"date\":\"2023-09-12\", \"owner\":2, \"participants\": [2, 5, 6, 7]}";
        mockMvc.perform(post("/api/v1/sessions").contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(99)))
                .andExpect(jsonPath("$.data[0].date", is("2023-09-12")))
                .andExpect(jsonPath("$.data[0].owner", is(2)))
                .andExpect(jsonPath("$.data[0].participants").isArray())
                .andExpect(jsonPath("$.data[0].participants", hasSize(4)))
                .andExpect(jsonPath("$.data[0].participants[0]", is(2)))
                .andExpect(jsonPath("$.data[0].participants[1]", is(5)))
                .andExpect(jsonPath("$.data[0].participants[2]", is(6)))
                .andExpect(jsonPath("$.data[0].participants[3]", is(7)));

        verify(sessionService, times(1)).createNewSession(
                eq(LocalDate.of(2023, 9, 12)),
                eq(2L),
                participantsCaptor.capture());
        final Set<Long> expectedParticipants = Stream.of(2L, 5L, 6L, 7L).collect(Collectors.toSet());
        final Iterator<Long> itr = participantsCaptor.getValue().iterator();
        while(itr.hasNext()) {
            final long id = itr.next();
            assertTrue(expectedParticipants.remove(id), "Unable to remove id " + id);
        }
        assertTrue(expectedParticipants.isEmpty());
    }

    /**
     * Given request body does not contain mandatory date field, when invoke POST /api/v1/sessions, fail and throw error
     * @throws Exception exception
     */
    @Test
    void givenRequestBodyWithoutDate_whenCreateNewSession_returnError() throws Exception {

        final String content = "{\"owner\":2, \"participants\": [2]}";
        mockMvc.perform(post("/api/v1/sessions").contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Date is mandatory")))
                .andExpect(jsonPath("$.path", is("/api/v1/sessions")));

        verifyNoInteractions(sessionService);
    }

    /**
     * Given request body does not contain mandatory owner field, when invoke POST /api/v1/sessions, fail and throw error
     * @throws Exception exception
     */
    @Test
    void givenRequestBodyWithoutOwnerId_whenCreateNewSession_returnError() throws Exception {

        final String content = "{\"date\":\"2023-09-12\", \"participants\": [2, 5, 6, 7]}";
        mockMvc.perform(post("/api/v1/sessions").contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Owner Id is mandatory")))
                .andExpect(jsonPath("$.path", is("/api/v1/sessions")));

        verifyNoInteractions(sessionService);
    }
}