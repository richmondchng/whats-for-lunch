package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.repository.SessionRepository;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantEntity;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantId;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantStatus;
import com.richmond.whatsforlunch.session.repository.entity.RestaurantEntity;
import com.richmond.whatsforlunch.session.repository.entity.RestaurantStatus;
import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import com.richmond.whatsforlunch.session.repository.entity.SessionStatus;
import com.richmond.whatsforlunch.session.service.dto.Participant;
import com.richmond.whatsforlunch.session.service.dto.Restaurant;
import com.richmond.whatsforlunch.session.service.dto.Session;
import com.richmond.whatsforlunch.users.repository.UserRepository;
import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit test SessionService
 */
@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    // test instance
    private SessionService sessionService;

    @Captor
    private ArgumentCaptor<Set<SessionStatus>> setStatusCaptor;

    @BeforeEach
    void setUp() {
        sessionService = new SessionService(sessionRepository, userRepository);
    }

    @AfterEach
    void tearDown() {
        sessionService = null;
    }

    /**
     * Given owner ID does not map to user, when creating new session, throw exception
     */
    @Test
    void givenOwnerIdIsNotValid_whenCreateNewSession_throwException() {
        // given
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());

        // when
        try {
            sessionService.createNewSession(LocalDate.of(2023, 9, 13), "ed", List.of(2L, 3L));
            fail("Expect exception to be thrown");
        } catch(RuntimeException ex) {
            assertEquals("Owner is not found", ex.getMessage());
        }

        // then
        verify(userRepository, times(1)).findByUserName(eq("ed"));
        verify(userRepository, times(0)).findAllById(anyCollection());
        verifyNoInteractions(sessionRepository);
    }

    /**
     * Given participant ID does not map to user, when creating new session, throw exception
     */
    @Test
    void givenParticipantIdIsNotValid_whenCreateNewSession_throwException() {
        // given
        final UserEntity u1 = UserEntity.builder().id(2L).userName("ed").build();
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(u1));
        when(userRepository.findAllById(anyCollection())).thenReturn(List.of(u1));

        // when
        try {
            sessionService.createNewSession(LocalDate.of(2023, 9, 13), "ed", List.of(2L, 3L));
            fail("Expect exception to be thrown");
        } catch(RuntimeException ex) {
            assertEquals("Participant is not found", ex.getMessage());
        }

        // then
        verify(userRepository, times(1)).findByUserName(eq("ed"));
        verify(userRepository, times(1)).findAllById(anyCollection());
        verifyNoInteractions(sessionRepository);
    }

    /**
     * Given valid inputs, when creating new session, create then return newly created session
     */
    @Test
    void givenValidInput_whenCreateNewSession_createAndReturnNewSession() {
        // given
        final UserEntity u1 = UserEntity.builder().id(2L).userName("ed").firstName("Edward").build();
        final UserEntity u2 = UserEntity.builder().id(3L).userName("betty").firstName("Betty").build();
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(u1));
        when(userRepository.findAllById(anyCollection())).thenReturn(List.of(u1, u2));
        doAnswer(invocationOnMock -> {
            final SessionEntity param = invocationOnMock.getArgument(0, SessionEntity.class);
            param.setId(998L);
            return param;
        }).when(sessionRepository).saveAndFlush(any());

        // when
        final Session result = sessionService.createNewSession(LocalDate.of(2023, 9, 13), "ed", List.of(2L, 3L));

        // then
        assertEquals(998L, result.id());
        assertEquals(LocalDate.of(2023, 9, 13), result.date());
        assertEquals("ACTIVE", result.status());
        assertEquals(2L, result.owner().id());
        assertEquals("ed", result.owner().userName());
        assertEquals("Edward", result.owner().displayName());

        final Iterator<Participant> itr = result.participants().stream()
                .sorted(Comparator.comparing(Participant::id)).toList().iterator();

        final Participant p1 = itr.next();
        assertEquals(2L, p1.id());
        assertEquals("ed", p1.userName());
        assertEquals("Edward", p1.displayName());
        assertEquals("PENDING", p1.status());

        final Participant p2 = itr.next();
        assertEquals(3L, p2.id());
        assertEquals("betty", p2.userName());
        assertEquals("Betty", p2.displayName());
        assertEquals("PENDING", p2.status());

        assertFalse(itr.hasNext());

        verify(userRepository, times(1)).findByUserName(eq("ed"));
        verify(userRepository, times(1)).findAllById(anyCollection());
        final ArgumentCaptor<SessionEntity> argumentCaptor = ArgumentCaptor.forClass(SessionEntity.class);
        verify(sessionRepository, times(1)).saveAndFlush(argumentCaptor.capture());

        final SessionEntity saved = argumentCaptor.getValue();
        assertEquals(LocalDate.of(2023, 9, 13), saved.getDate());
        assertEquals(2L, saved.getOwner().getId());
        assertEquals(SessionStatus.ACTIVE, saved.getStatus());

        final Iterator<ParticipantEntity> itrE = saved.getParticipants().stream()
                .sorted(Comparator.comparing(p -> p.getUser().getId())).toList().iterator();
        final ParticipantEntity pe1 = itrE.next();
        assertEquals(2L, pe1.getUser().getId());
        assertEquals(ParticipantStatus.PENDING, pe1.getStatus());

        final ParticipantEntity pe2 = itrE.next();
        assertEquals(3L, pe2.getUser().getId());
        assertEquals(ParticipantStatus.PENDING, pe2.getStatus());

        assertFalse(itrE.hasNext());
    }
    /**
     * Given username, when get sessions by user, return sessions
     */
    @Test
    void givenUsername_whenGetSessionsByUser_returnSessionsByUser() {
        // given
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(UserEntity.builder().id(22L).build()));
        when(sessionRepository.findByUserNameAndStatus(anyLong(), anySet())).thenReturn(
                List.of(SessionEntity.builder()
                        .id(3L).date(LocalDate.of(2023, 9, 12))
                        .owner(UserEntity.builder().id(2L).build())
                        .participants(Collections.emptyList())
                        .restaurants(Collections.emptyList())
                        .status(SessionStatus.ACTIVE)
                        .build())
        );

        // when
        final List<Session> results = sessionService.getSessionsByUser("andy", List.of("ACTIVE"));

        // then
        assertEquals(1, results.size());
        assertEquals(3L, results.get(0).id());

        verify(userRepository, times(1)).findByUserName("andy");
        verify(sessionRepository, times(1)).findByUserNameAndStatus(eq(22L), setStatusCaptor.capture());
        final Set<SessionStatus> paramStatus = setStatusCaptor.getValue();
        assertEquals(1, paramStatus.size());
        assertTrue(paramStatus.contains(SessionStatus.ACTIVE));
    }

    /**
     * Given username, when get sessions by user, return sessions
     */
    @Test
    void givenInvalidUser_whenGetSessionsByUser_returnSessionsByUser() {
        // given
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());

        // when
        try {
            sessionService.getSessionsByUser("andy", List.of("ACTIVE"));
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("User is not valid", e.getMessage());
        }

        // then
        verify(userRepository, times(1)).findByUserName("andy");
        verifyNoInteractions(sessionRepository);
    }

    /**
     * Given session ID is invalid, when getSessionById, throw exception when repository return empty
     */
    @Test
    void givenInvalidSessionId_whenGetSessionById_throwException() {
        // given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        try {
            sessionService.getSessionById(999L);
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("Session ID is invalid", e.getMessage());
        }

        // then
        verify(sessionRepository).findById(999L);
    }

    /**
     * Given session ID, when getSessionById, return session object
     */
    @Test
    void givenSessionId_whenGetSessionById_throwException() {
        // given
        final UserEntity owner = UserEntity.builder().id(2L).userName("ed").firstName("Edward").lastName("Goh").build();
        final SessionEntity session = SessionEntity.builder()
                .id(99L).date(LocalDate.of(2023, 9, 12))
                .owner(owner)
                .participants(new ArrayList<>(1))
                .restaurants(new ArrayList<>(1))
                .status(SessionStatus.ACTIVE)
                .build();
        session.getParticipants().add(ParticipantEntity.builder()
                .id(new ParticipantId(3L, 2L)).session(session).user(owner)
                .status(ParticipantStatus.PENDING).build());
        session.getRestaurants().add(RestaurantEntity.builder()
                .id(20L).addedByUser(2L).restaurantName("McFried Chicken").description("Yummy fried chicken")
                .status(RestaurantStatus.ACTIVE).build());
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // when
        final Session result = sessionService.getSessionById(99L);

        // then
        verify(sessionRepository, times(1)).findById(eq(99L));

        // test result is mapped correctly
        assertEquals(99L, result.id());
        assertEquals("ACTIVE", result.status());
        assertEquals(LocalDate.of(2023, 9, 12), result.date());

        assertEquals(2L, result.owner().id());
        assertEquals("ed", result.owner().userName());
        assertEquals("Edward", result.owner().displayName());

        assertEquals(1, result.participants().size());
        final Participant participant = result.participants().get(0);
        assertEquals(2L, participant.id());
        assertEquals("ed", participant.userName());
        assertEquals("Edward", participant.displayName());
        assertEquals("PENDING", participant.status());

        assertEquals(1, result.restaurants().size());
        final Restaurant restaurant = result.restaurants().get(0);
        assertEquals(20L, restaurant.id());
        assertEquals(2L, restaurant.userId());
        assertEquals("McFried Chicken", restaurant.restaurant());
        assertEquals("Yummy fried chicken", restaurant.description());
        assertEquals("ACTIVE", restaurant.status());
    }

    /**
     * Given session ID provided is invalid, when deleteSession, throw exception
     */
    @Test
    void givenSessionIdInvalid_whenDeleteSession_throwException() {
        // given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        try {
            sessionService.deleteSession(99999L, "steven");
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("Session ID is invalid", e.getMessage());
        }

        // verify
        verify(sessionRepository, times(1)).findById(eq(99999L));
        verify(sessionRepository, times(0)).saveAndFlush(any());
    }

    /**
     * Given current user is not owner, when deleteSession, throw exception
     */
    @Test
    void givenNotOwner_whenDeleteSession_throwException() {
        // given
        final UserEntity owner = UserEntity.builder().id(2L).userName("ed").firstName("Edward").build();
        final SessionEntity session = SessionEntity.builder()
                .id(15L).date(LocalDate.of(2023, 9, 13))
                // session is closed
                .version(0).owner(owner).status(SessionStatus.ACTIVE)
                .build();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // when
        try {
            sessionService.deleteSession(12L, "steven");
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("Current user is not the session owner", e.getMessage());
        }

        // verify
        verify(sessionRepository, times(1)).findById(eq(12L));
        verify(sessionRepository, times(0)).saveAndFlush(any());
    }

    /**
     * Given session ID provided, when deleteSession, flag session as deleted
     */
    @Test
    void givenSessionId_whenDeleteSession_flagSessionAsDeleted() {
        // given
        final UserEntity owner = UserEntity.builder().id(2L).userName("ed").firstName("Edward").build();
        final SessionEntity session = SessionEntity.builder()
                .id(15L).date(LocalDate.of(2023, 9, 13))
                // session is closed
                .version(0).owner(owner).status(SessionStatus.ACTIVE)
                .build();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // when
        sessionService.deleteSession(15L, "ed");

        // verify
        verify(sessionRepository, times(1)).findById(eq(15L));
        final ArgumentCaptor<SessionEntity> captor = ArgumentCaptor.forClass(SessionEntity.class);
        verify(sessionRepository, times(1)).saveAndFlush(captor.capture());
        final SessionEntity result = captor.getValue();
        assertEquals(15L, session.getId());
        assertEquals(SessionStatus.DELETED, session.getStatus());
    }
}