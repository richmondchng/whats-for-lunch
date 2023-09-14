package com.richmond.whatsforlunch.session.service.strategy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantSelectionStrategyFactoryTest {

    @Mock
    private RestaurantSelectionStrategy restaurantSelectionStrategy1;
    @Mock
    private RestaurantSelectionStrategy restaurantSelectionStrategy2;
    @Mock
    private RestaurantSelectionStrategy restaurantSelectionStrategy3;

    // test instance
    private RestaurantSelectionStrategyFactory restaurantSelectionStrategyFactory;

    @BeforeEach
    void setUp() {
        when(restaurantSelectionStrategy1.getName()).thenReturn("STRATEGY_1");
        when(restaurantSelectionStrategy2.getName()).thenReturn("STRATEGY_2");
        when(restaurantSelectionStrategy3.getName()).thenReturn("STRATEGY_3");

        restaurantSelectionStrategyFactory = new RestaurantSelectionStrategyFactory(List.of(
                restaurantSelectionStrategy1, restaurantSelectionStrategy2, restaurantSelectionStrategy3));
    }

    @AfterEach
    void tearDown() {
        restaurantSelectionStrategyFactory = null;
    }

    /**
     * Given strategy name matched, when getStrategy, return mapped Object
     */
    @Test
    void givenStrategyExist_whenGetStrategy_returnStrategy() {
        final RestaurantSelectionStrategy result = restaurantSelectionStrategyFactory.getStrategy("STRATEGY_2");
        assertNotNull(result);
        assertEquals("STRATEGY_2", result.getName());
    }

    /**
     * Given strategy name does not matched, when getStrategy, throw exception
     */
    @Test
    void givenStrategyDoesNotExist_whenGetStrategy_throwException() {
        try {
            restaurantSelectionStrategyFactory.getStrategy("STRATEGY_22");
            fail("Expect exception to be thrown");
        } catch(RuntimeException e) {
            assertEquals("Strategy is not found", e.getMessage());
        }
    }
}