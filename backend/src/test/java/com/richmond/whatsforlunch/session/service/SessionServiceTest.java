package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.repository.ParticipantRepository;
import com.richmond.whatsforlunch.session.repository.SessionRepository;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantEntity;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantStatus;
import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import com.richmond.whatsforlunch.session.repository.entity.SessionStatus;
import com.richmond.whatsforlunch.session.service.dto.Participant;
import com.richmond.whatsforlunch.session.service.dto.Session;
import com.richmond.whatsforlunch.users.repository.UserRepository;
import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
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

    @Mock
    private ParticipantRepository participantRepository;

    // test instance
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        sessionService = new SessionService(sessionRepository, userRepository, participantRepository);
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
        when(userRepository.findAllById(anyCollection())).thenReturn(Collections.emptyList());

        // when
        try {
            sessionService.createNewSession(LocalDate.of(2023, 9, 13), 2L, List.of(2L, 3L));
            fail("Expect exception to be thrown");
        } catch(RuntimeException ex) {
            assertEquals("Owner is not found", ex.getMessage());
        }

        // then
        verify(userRepository, times(1)).findAllById(anyCollection());
        verifyNoInteractions(sessionRepository);
    }

    /**
     * Given participant ID does not map to user, when creating new session, throw exception
     */
    @Test
    void givenParticipantIdIsNotValid_whenCreateNewSession_throwException() {
        // given
        when(userRepository.findAllById(anyCollection())).thenReturn(List.of(
                UserEntity.builder().id(2L).userName("ed").build()
        ));

        // when
        try {
            sessionService.createNewSession(LocalDate.of(2023, 9, 13), 2L, List.of(2L, 3L));
            fail("Expect exception to be thrown");
        } catch(RuntimeException ex) {
            assertEquals("Participant is not found", ex.getMessage());
        }

        // then
        verify(userRepository, times(1)).findAllById(anyCollection());
        verifyNoInteractions(sessionRepository);
    }

    /**
     * Given valid inputs, when creating new session, create then return newly created session
     */
    @Test
    void givenValidInput_whenCreateNewSession_createAndReturnNewSession() {
        // given
        when(userRepository.findAllById(anyCollection())).thenReturn(List.of(
                UserEntity.builder().id(2L).userName("ed").firstName("Edward").build(),
                UserEntity.builder().id(3L).userName("betty").firstName("Betty").build()
        ));
        doAnswer(invocationOnMock -> {
            final SessionEntity param = invocationOnMock.getArgument(0, SessionEntity.class);
            param.setId(998L);
            return param;
        }).when(sessionRepository).saveAndFlush(any());

        // when
        final Session result = sessionService.createNewSession(LocalDate.of(2023, 9, 13), 2L, List.of(2L, 3L));

        // then
        assertEquals(998L, result.id());
        assertEquals("OPEN", result.status());
        assertEquals(LocalDate.of(2023, 9, 13), result.date());
        assertEquals("OPEN", result.status());
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

        verify(userRepository, times(1)).findAllById(anyCollection());
        final ArgumentCaptor<SessionEntity> argumentCaptor = ArgumentCaptor.forClass(SessionEntity.class);
        verify(sessionRepository, times(1)).saveAndFlush(argumentCaptor.capture());

        final SessionEntity saved = argumentCaptor.getValue();
        assertEquals(LocalDate.of(2023, 9, 13), saved.getDate());
        assertEquals(2L, saved.getOwner().getId());
        assertEquals(SessionStatus.OPEN, saved.getStatus());

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
}