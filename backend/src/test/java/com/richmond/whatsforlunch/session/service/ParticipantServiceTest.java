package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.repository.SessionRepository;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantEntity;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantId;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantStatus;
import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import com.richmond.whatsforlunch.session.repository.entity.SessionStatus;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test ParticipantService.
 */
@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;

    private ParticipantService participantService;

    @BeforeEach
    void setUp() {
        participantService = new ParticipantService(sessionRepository, userRepository);
    }

    @AfterEach
    void tearDown() {
        participantService = null;
    }

    /**
     * Given session does not exist, when addParticipantsToSession, throw exception
     */
    @Test
    void givenSessionDoesNotExists_whenAddParticipantsToSession_throwException() {
        // given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        try {
            participantService.addParticipantsToSession(999L, List.of(6L, 7L));
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("Session ID is invalid", e.getMessage());
        }

        // then
        verify(sessionRepository, times(1)).findById(eq(999L));
    }

    /**
     * Given session is not active, when addParticipantsToSession, throw exception
     */
    @Test
    void givenSessionIsNotActive_whenAddParticipantsToSession_throwException() {
        // given
        final UserEntity owner = UserEntity.builder().id(2L).userName("ed").firstName("Edward").build();
        final SessionEntity session = SessionEntity.builder()
                .id(15L).date(LocalDate.of(2023, 9, 13)).status(SessionStatus.ACTIVE)
                // session is closed
                .version(0).owner(owner).status(SessionStatus.CLOSED)
                .build();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // when
        try {
            participantService.addParticipantsToSession(15L, List.of(6L, 7L));
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("Session is active", e.getMessage());
        }

        // then
        verify(sessionRepository, times(1)).findById(eq(15L));
    }

    /**
     * Given valid parameters, when addParticipantsToSession, add participants to session
     */
    @Test
    void givenSessionListOfParticipants_whenAddParticipantsToSession_addParticipantsToSession() {
        // given
        final UserEntity owner = UserEntity.builder().id(2L).userName("ed").firstName("Edward").build();
        final SessionEntity session = SessionEntity.builder()
                .id(15L).date(LocalDate.of(2023, 9, 13)).status(SessionStatus.ACTIVE)
                // session is closed
                .version(0).owner(owner).status(SessionStatus.ACTIVE)
                .participants(new ArrayList<>())
                .build();
        session.getParticipants().add(ParticipantEntity.builder()
                .id(new ParticipantId(15L, 2L)).user(owner).session(session)
                .status(ParticipantStatus.PENDING).build());
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        final UserEntity user1 = UserEntity.builder().id(6L).userName("peggy").firstName("Peggy").lastName("Ng").build();
        final UserEntity user2 = UserEntity.builder().id(7L).userName("peter").firstName("Peter").lastName("Chan").build();
        when(userRepository.findAllById(anyList())).thenReturn(List.of(user1, user2));

        // when
        participantService.addParticipantsToSession(15L, List.of(2L, 6L, 7L, 9L));

        // then
        verify(sessionRepository, times(1)).findById(eq(15L));
        verify(userRepository, times(1)).findAllById(List.of(6L, 7L, 9L));

        final ArgumentCaptor<SessionEntity> captor = ArgumentCaptor.forClass(SessionEntity.class);
        verify(sessionRepository, times(1)).saveAndFlush(captor.capture());
        final SessionEntity result = captor.getValue();
        // get a sorted iterator
        final Iterator<ParticipantEntity> itr = result.getParticipants().stream()
                .sorted(Comparator.comparing(p -> p.getUser().getId())).iterator();

        // already existing participant
        final ParticipantEntity participant1 = itr.next();
        assertEquals(2L, participant1.getId().getUserId());

        final ParticipantEntity participant2 = itr.next();
        assertEquals(15L, participant2.getId().getSessionId());
        assertEquals(6L, participant2.getId().getUserId());
        assertEquals(15L, participant2.getSession().getId());
        assertEquals(6L, participant2.getUser().getId());
        assertEquals(ParticipantStatus.PENDING, participant2.getStatus());

        final ParticipantEntity participant3 = itr.next();
        assertEquals(15L, participant3.getId().getSessionId());
        assertEquals(7L, participant3.getId().getUserId());
        assertEquals(15L, participant3.getSession().getId());
        assertEquals(7L, participant3.getUser().getId());
        assertEquals(ParticipantStatus.PENDING, participant3.getStatus());

        // only 2 participants since only 2 are valid User ID.
        assertFalse(itr.hasNext());
    }
}