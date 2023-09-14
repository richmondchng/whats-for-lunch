package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.repository.SessionRepository;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantEntity;
import com.richmond.whatsforlunch.session.repository.entity.ParticipantId;
import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import com.richmond.whatsforlunch.session.repository.entity.SessionStatus;
import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit test RestaurantService
 */
@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        restaurantService = new RestaurantService(sessionRepository);
    }

    @AfterEach
    void tearDown() {
        restaurantService = null;
    }

    /**
     * Given restaurant name is greater than 255 characters, when addRestaurantToSession, throw exception
     */
    @Test
    void givenRestaurantNameMoreThan255Char_whenAddRestaurantToSession_throwException() {
        final String nameMoreThan255 = "McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken McFly Chicken";
        // when
        try {
            restaurantService.addRestaurantToSession(15L, 3L, nameMoreThan255, "Yummy fried chicken");
        } catch(RuntimeException e) {
            assertEquals("Restaurant name is too long (max 255 character)", e.getMessage());
        }

        // verify
        verifyNoInteractions(sessionRepository);
    }

    /**
     * Given description is greater than 255 characters, when addRestaurantToSession, throw exception
     */
    @Test
    void givenDescriptionMoreThan255Char_whenAddRestaurantToSession_throwException() {
        final String descMoreThan255 = "Yummy fried chicken Yummy fried chicken Yummy fried chicken Yummy fried chicken Yummy fried chicken Yummy fried chicken Yummy fried chicken Yummy fried chicken Yummy fried chicken Yummy fried chicken Yummy fried chicken Yummy fried chicken Yummy fried chicken";
        // when
        try {
            restaurantService.addRestaurantToSession(15L, 3L, "McFly Chicken", descMoreThan255);
        } catch(RuntimeException e) {
            assertEquals("Description is too long (max 255 character)", e.getMessage());
        }

        // verify
        verifyNoInteractions(sessionRepository);
    }

    /**
     * Given session ID provided is invalid, when addRestaurantToSession, throw exception
     */
    @Test
    void givenSessionIdInvalid_whenAddRestaurantToSession_throwException() {
        // given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        try {
            restaurantService.addRestaurantToSession(99999L, 3L, "McFly Chicken", "Yummy fried chicken");
        } catch(RuntimeException e) {
            assertEquals("Session ID is invalid", e.getMessage());
        }

        // verify
        verify(sessionRepository, times(1)).findById(eq(99999L));
    }

    /**
     * Given session is not open, when addRestaurantToSession, throw exception
     */
    @Test
    void givenSessionIsNotOpen_whenAddRestaurantToSession_throwException() {

        final UserEntity owner = UserEntity.builder().id(2L).userName("ed").firstName("Edward").build();
        final SessionEntity session = SessionEntity.builder()
                .id(15L).date(LocalDate.of(2023, 9, 13)).status(SessionStatus.ACTIVE)
                // session is closed
                .version(0).owner(owner).status(SessionStatus.CLOSED)
                .build();
        // given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // when
        try {
            restaurantService.addRestaurantToSession(15L, 4L, "McFly Chicken", "Yummy fried chicken");
        } catch(RuntimeException e) {
            assertEquals("Session is not open for submission", e.getMessage());
        }

        // verify
        verify(sessionRepository, times(1)).findById(eq(15L));
    }

    /**
     * Given user is not a participant, when addRestaurantToSession, throw exception
     */
    @Test
    void givenUserIsNotParticipant_whenAddRestaurantToSession_throwException() {
        final UserEntity owner = UserEntity.builder().id(2L).userName("ed").firstName("Edward").build();
        final UserEntity participantUser1 = UserEntity.builder().id(3L).userName("peggy").firstName("Peggy").build();

        final ParticipantEntity participant1 = ParticipantEntity.builder().id(new ParticipantId(15L, 2L)).user(owner).build();
        final ParticipantEntity participant2 = ParticipantEntity.builder().id(new ParticipantId(15L, 3L)).user(participantUser1).build();

        final SessionEntity session = SessionEntity.builder()
                .id(15L).date(LocalDate.of(2023, 9, 13)).status(SessionStatus.ACTIVE)
                .version(0).owner(owner).participants(List.of(participant1, participant2))
                .build();
        // given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // when
        try {
            restaurantService.addRestaurantToSession(15L, 4L, "McFly Chicken", "Yummy fried chicken");
        } catch(RuntimeException e) {
            assertEquals("User is not a participant", e.getMessage());
        }

        // verify
        verify(sessionRepository, times(1)).findById(eq(15L));
    }
}