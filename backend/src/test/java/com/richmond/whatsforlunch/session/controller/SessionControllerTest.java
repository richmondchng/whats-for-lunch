package com.richmond.whatsforlunch.session.controller;

import com.richmond.whatsforlunch.common.config.TestSecurityConfig;
import com.richmond.whatsforlunch.session.repository.entity.SessionStatus;
import com.richmond.whatsforlunch.session.service.SelectionService;
import com.richmond.whatsforlunch.session.service.SessionService;
import com.richmond.whatsforlunch.session.service.dto.Owner;
import com.richmond.whatsforlunch.session.service.dto.Participant;
import com.richmond.whatsforlunch.session.service.dto.Restaurant;
import com.richmond.whatsforlunch.session.service.dto.Session;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test SessionController
 */
@WebMvcTest(controllers = { SessionController.class })
@Import({ TestSecurityConfig.class })
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;
    @MockBean
    private SelectionService selectionService;

    @Captor
    private ArgumentCaptor<List<Long>> participantsCaptor;

    /**
     * Given request body is valid, when invoke POST /api/v1/sessions, return created session
     * @throws Exception exception
     */
    @Test
    void givenRequestBodyIsValid_whenCreateNewSession_returnNewSession() throws Exception {

        when(sessionService.createNewSession(any(LocalDate.class), anyLong(), anyList())).thenReturn(
          new Session(99L, LocalDate.of(2023, 9, 12),
                  new Owner(2L, "ed", "Edward"),
                  List.of(new Participant(2L, "ed", "Edward", "PENDING"),
                          new Participant(5L, "peggy", "Peggy", "PENDING")),
                  Collections.emptyList(), 0L, SessionStatus.ACTIVE.getName(), 1)
        );

        final String content = "{\"date\":\"2023-09-12\", \"owner\":2, \"participants\": [2, 5]}";
        mockMvc.perform(post("/api/v1/sessions").contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(99)))
                .andExpect(jsonPath("$.data[0].date", is("2023-09-12")))
                .andExpect(jsonPath("$.data[0].owner.id", is(2)))
                .andExpect(jsonPath("$.data[0].owner.userName", is("ed")))
                .andExpect(jsonPath("$.data[0].owner.displayName", is("Edward")))

                .andExpect(jsonPath("$.data[0].participants").isArray())
                .andExpect(jsonPath("$.data[0].participants", hasSize(2)))

                .andExpect(jsonPath("$.data[0].participants[0].id", is(2)))
                .andExpect(jsonPath("$.data[0].participants[0].userName", is("ed")))
                .andExpect(jsonPath("$.data[0].participants[0].displayName", is("Edward")))
                .andExpect(jsonPath("$.data[0].participants[0].status", is("PENDING")))

                .andExpect(jsonPath("$.data[0].participants[1].id", is(5)))
                .andExpect(jsonPath("$.data[0].participants[1].userName", is("peggy")))
                .andExpect(jsonPath("$.data[0].participants[1].displayName", is("Peggy")))
                .andExpect(jsonPath("$.data[0].participants[1].status", is("PENDING")))

                .andExpect(jsonPath("$.data[0].restaurants").isArray())
                .andExpect(jsonPath("$.data[0].restaurants", hasSize(0)))
                .andExpect(jsonPath("$.data[0].status", is(SessionStatus.ACTIVE.getName())))
                .andExpect(jsonPath("$.data[0].selectedRestaurant", is(0)))
        ;

        verify(sessionService, times(1)).createNewSession(
                eq(LocalDate.of(2023, 9, 12)),
                eq(2L),
                participantsCaptor.capture());
        final Set<Long> expectedParticipants = Stream.of(2L, 5L).collect(Collectors.toSet());
        for (long id : participantsCaptor.getValue()) {
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

    /**
     * Given request without owner or participant parameters, when invoke GET /api/v1/sessions, return empty list
     * @throws Exception exception
     */
    @Test
    void givenRequestWithoutUserIdParam_whenGetSessions_defaultStatus() throws Exception {

        mockMvc.perform(get("/api/v1/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(0)));

        verifyNoInteractions(sessionService);
    }

    /**
     * Given request with owner ID parameter, when invoke GET /api/v1/sessions, return collection
     * @throws Exception exception
     */
    @Test
    void givenRequestWithOwnerIdParam_whenGetSessions_returnOwnerSessionWithDefaultStatus() throws Exception {

        when(sessionService.getSessionsByOwner(anyLong(), any())).thenReturn(
                List.of(new Session(99L, LocalDate.of(2023, 9, 12),
                            new Owner(2L, "ed", "Edward"),
                            List.of(new Participant(2L, "ed", "Edward", "PENDING"),
                                new Participant(5L, "peggy", "Peggy", "PENDING")),
                            Collections.emptyList(), 0L, SessionStatus.ACTIVE.getName(), 1))
        );

        mockMvc.perform(get("/api/v1/sessions?owner=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(99)))
                .andExpect(jsonPath("$.data[0].date", is("2023-09-12")));

        verify(sessionService, times(1)).getSessionsByOwner(2L, List.of("ACTIVE","CLOSED"));
    }

    /**
     * Given request with participant ID parameter, when invoke GET /api/v1/sessions, return collection
     * @throws Exception exception
     */
    @Test
    void givenRequestWithParticipantIdParam_whenGetSessions_returnParticipantSessionWithDefaultStatus() throws Exception {

        when(sessionService.getSessionsByParticipant(anyLong(), any())).thenReturn(
                List.of(new Session(99L, LocalDate.of(2023, 9, 12),
                        new Owner(2L, "ed", "Edward"),
                        List.of(new Participant(2L, "ed", "Edward", "PENDING"),
                                new Participant(5L, "peggy", "Peggy", "PENDING")),
                                Collections.emptyList(), 0L, SessionStatus.ACTIVE.getName(), 1))
        );

        mockMvc.perform(get("/api/v1/sessions?participant=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(99)))
                .andExpect(jsonPath("$.data[0].date", is("2023-09-12")));

        verify(sessionService, times(1)).getSessionsByParticipant(5L, List.of("ACTIVE","CLOSED"));
    }

    /**
     * Given request with status parameter, when invoke GET /api/v1/sessions, return collection
     * @throws Exception exception
     */
    @Test
    void givenRequestWithStatusParam_whenGetSessions_returnOwnerSessionWithSelectedStatus() throws Exception {

        when(sessionService.getSessionsByParticipant(anyLong(), any())).thenReturn(
                List.of(new Session(99L, LocalDate.of(2023, 9, 12),
                        new Owner(2L, "ed", "Edward"),
                        List.of(new Participant(2L, "ed", "Edward", "PENDING"),
                                new Participant(5L, "peggy", "Peggy", "PENDING")),
                                Collections.emptyList(), 0L, SessionStatus.ACTIVE.getName(), 1))
        );

        mockMvc.perform(get("/api/v1/sessions?participant=5&status=CLOSED,DELETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(99)))
                .andExpect(jsonPath("$.data[0].date", is("2023-09-12")));

        verify(sessionService, times(1)).getSessionsByParticipant(5L, List.of("CLOSED", "DELETED"));
    }


    /**
     * Given path variable session ID is 0, when invoke GET /api/v1/sessions/{id}, throw error
     * @throws Exception exception
     */
    @Test
    void givenSessionIdZero_whenGetSessionById_returnError() throws Exception {

        mockMvc.perform(get("/api/v1/sessions/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Session ID is mandatory")))
                .andExpect(jsonPath("$.path", is("/api/v1/sessions/0")));

        verifyNoInteractions(sessionService);
    }

    /**
     * Given path variable session ID is 0, when invoke GET /api/v1/sessions/{id}, throw error
     * @throws Exception exception
     */
    @Test
    void givenSessionId_whenGetSessionById_returnSessionDetails() throws Exception {

        final Owner owner = new Owner(2L, "ed", "Edward");
        final Participant participant1 = new Participant(2L, "ed", "Edward", "PENDING");
        final Restaurant restaurant1 = new Restaurant(5L, 2L, "Brian's Eatery", "Fusion food", "ACTIVE");
        final Session session = new Session(99L, LocalDate.of(2023, 9, 12),
                owner, List.of(participant1), List.of(restaurant1), 0L, SessionStatus.ACTIVE.getName(), 1);
        when(sessionService.getSessionById(anyLong())).thenReturn(session);

        mockMvc.perform(get("/api/v1/sessions/99"))
                .andExpect(status().isOk())

                // test data is mapped correctly
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(99)))
                .andExpect(jsonPath("$.data[0].date", is("2023-09-12")))
                .andExpect(jsonPath("$.data[0].owner.id", is(2)))
                .andExpect(jsonPath("$.data[0].owner.userName", is("ed")))
                .andExpect(jsonPath("$.data[0].owner.displayName", is("Edward")))

                .andExpect(jsonPath("$.data[0].participants").isArray())
                .andExpect(jsonPath("$.data[0].participants", hasSize(1)))

                .andExpect(jsonPath("$.data[0].participants[0].id", is(2)))
                .andExpect(jsonPath("$.data[0].participants[0].userName", is("ed")))
                .andExpect(jsonPath("$.data[0].participants[0].displayName", is("Edward")))
                .andExpect(jsonPath("$.data[0].participants[0].status", is("PENDING")))

                .andExpect(jsonPath("$.data[0].restaurants").isArray())
                .andExpect(jsonPath("$.data[0].restaurants", hasSize(1)))
                .andExpect(jsonPath("$.data[0].restaurants[0].id", is(5)))
                .andExpect(jsonPath("$.data[0].restaurants[0].addedBy", is(2)))
                .andExpect(jsonPath("$.data[0].restaurants[0].restaurantName", is("Brian's Eatery")))
                .andExpect(jsonPath("$.data[0].restaurants[0].description", is("Fusion food")))
                .andExpect(jsonPath("$.data[0].restaurants[0].status", is("ACTIVE")))

                .andExpect(jsonPath("$.data[0].status", is(SessionStatus.ACTIVE.getName())))
                .andExpect(jsonPath("$.data[0].selectedRestaurant", is(0)))
        ;

        verify(sessionService, times(1)).getSessionById(eq(99L));
    }

    /**
     * Given request is valid, when invoke DELETE /api/v1/sessions/{sessionId}, return success status
     * @throws Exception exception
     */
    @Test
    void givenRequestIsValid_whenDeleteSession_returnSuccessStatus() throws Exception {

        mockMvc.perform(delete("/api/v1/sessions/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].status", is("Success")));

        verify(sessionService, times(1)).deleteSession(2L);
    }

    /**
     * Given path session ID is zero, when invoke POST /api/v1/sessions/{sessionId}, fail and throw error
     * @throws Exception exception
     */
    @Test
    void givenSessionIdIsZero_whenDeleteSession_returnError() throws Exception {

        mockMvc.perform(delete("/api/v1/sessions/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Session ID is mandatory")))
                .andExpect(jsonPath("$.path", is("/api/v1/sessions/0")));

        verifyNoInteractions(sessionService);
    }

    /**
     * Given request, when invoke PATCH /api/v1/sessions/{id}, select restaurant and close session
     * @throws Exception exception
     */
    @Test
    void givenValidRequest_whenPatchSession_returnSessionDetails() throws Exception {

        final Restaurant restaurant1 = new Restaurant(5L, 2L, "Brian's Eatery", "Fusion food", "ACTIVE");
        when(selectionService.selectRestaurant(anyLong(), anyString())).thenReturn(restaurant1);

        final String content = "{\"strategy\":\"RANDOM\"}";
        mockMvc.perform(patch("/api/v1/sessions/99").contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].sessionId", is(99)))
                .andExpect(jsonPath("$.data[0].restaurantId", is(5)))
                .andExpect(jsonPath("$.data[0].restaurantName", is("Brian's Eatery")));

        verify(selectionService, times(1)).selectRestaurant(eq(99L), eq("RANDOM"));
    }
}