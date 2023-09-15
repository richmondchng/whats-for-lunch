package com.richmond.whatsforlunch.session.service;

import com.richmond.whatsforlunch.session.repository.SessionRepository;
import com.richmond.whatsforlunch.session.repository.entity.RestaurantEntity;
import com.richmond.whatsforlunch.session.repository.entity.RestaurantStatus;
import com.richmond.whatsforlunch.session.repository.entity.SessionEntity;
import com.richmond.whatsforlunch.session.repository.entity.SessionStatus;
import com.richmond.whatsforlunch.session.service.dto.Restaurant;
import com.richmond.whatsforlunch.session.service.strategy.RestaurantSelectionStrategy;
import com.richmond.whatsforlunch.session.service.strategy.RestaurantSelectionStrategyFactory;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test SelectionService.
 */
@ExtendWith(MockitoExtension.class)
class SelectionServiceTest {

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private RestaurantSelectionStrategy restaurantSelectionStrategy;
    private SelectionService selectionService;

    @BeforeEach
    void setUp() {
        when(restaurantSelectionStrategy.getName()).thenReturn("RANDOM");
        selectionService = new SelectionService(sessionRepository,
                new RestaurantSelectionStrategyFactory(List.of(restaurantSelectionStrategy)));
    }

    @AfterEach
    void selectionService() {
        selectionService = null;
    }

    /**
     * Given session ID provided is invalid, when selectRestaurant, throw exception
     */
    @Test
    void givenSessionIdInvalid_whenSelectRestaurant_throwException() {
        // given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        try {
            selectionService.selectRestaurant(99999L, "RANDOM");
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("Session ID is invalid", e.getMessage());
        }

        // verify
        verify(sessionRepository, times(1)).findById(eq(99999L));
        verify(sessionRepository, times(0)).saveAndFlush(any());
    }

    /**
     * Given session is not open, when selectRestaurant, throw exception
     */
    @Test
    void givenSessionIsNotOpen_whenSelectRestaurant_throwException() {

        final UserEntity owner = UserEntity.builder().id(2L).userName("ed").firstName("Edward").build();
        final SessionEntity session = SessionEntity.builder()
                .id(15L).date(LocalDate.of(2023, 9, 13))
                // session is closed
                .version(0).owner(owner).status(SessionStatus.CLOSED)
                .build();
        // given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // when
        try {
            selectionService.selectRestaurant(15L, "RANDOM");
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("Session is not active", e.getMessage());
        }

        // verify
        verify(sessionRepository, times(1)).findById(eq(15L));
        verify(sessionRepository, times(0)).saveAndFlush(any());
    }

    /**
     * Given no restaurant in session, when selectRestaurant, throw exception
     */
    @Test
    void givenNoRestaurantInSession_whenSelectRestaurant_throwException() {
        // given
        final UserEntity owner = UserEntity.builder().id(2L).userName("ed").firstName("Edward").build();
        final SessionEntity session = SessionEntity.builder()
                .id(15L).date(LocalDate.of(2023, 9, 13)).status(SessionStatus.ACTIVE)
                .version(0).owner(owner).status(SessionStatus.ACTIVE)
                .restaurants(Collections.emptyList())
                .selectedRestaurant(0L)
                .build();
        // given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // when
        try {
            selectionService.selectRestaurant(99999L, "RANDOM");
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("No restaurant in session, please add one restaurant to continue", e.getMessage());
        }

        // verify
        verify(sessionRepository, times(1)).findById(eq(99999L));
        verify(sessionRepository, times(0)).saveAndFlush(any());
    }

    /**
     * Given list of restaurants, when selectRestaurant, select one restaurant and close session
     */
    @Test
    void givenListOfRestaurants_whenSelectRestaurant_useStrategyToSelectAndCloseSession() {

        final UserEntity owner = UserEntity.builder().id(2L).userName("ed").firstName("Edward").build();
        final RestaurantEntity restaurant = RestaurantEntity.builder().id(5L).addedByUser(2L)
                .restaurantName("McFreddie Burgers").description("Very nice burgers").build();
        final SessionEntity session = SessionEntity.builder()
                .id(15L).date(LocalDate.of(2023, 9, 13)).status(SessionStatus.ACTIVE)
                .version(0).owner(owner).status(SessionStatus.ACTIVE)
                .restaurants(List.of(restaurant))
                .selectedRestaurant(0L)
                .build();
        // given
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(restaurantSelectionStrategy.pickRestaurant(anyList())).thenReturn(
                new Restaurant(5L, 2L, "McFreddie Burgers", "Very nice burgers",
                        RestaurantStatus.ACTIVE.name()));

        // when
        final Restaurant result = selectionService.selectRestaurant(15L, "RANDOM");

        // verify
        assertEquals(5L, result.id());
        assertEquals("McFreddie Burgers", result.restaurant());

        verify(sessionRepository, times(1)).findById(eq(15L));

        final ArgumentCaptor<SessionEntity> captor = ArgumentCaptor.forClass(SessionEntity.class);
        verify(sessionRepository, times(1)).saveAndFlush(captor.capture());
        final SessionEntity captured = captor.getValue();
        assertEquals(5L, captured.getSelectedRestaurant());
        assertEquals(SessionStatus.CLOSED, captured.getStatus());
    }
}