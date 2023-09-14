package com.richmond.whatsforlunch.session.service.strategy;

import com.richmond.whatsforlunch.session.repository.entity.RestaurantStatus;
import com.richmond.whatsforlunch.session.service.dto.Restaurant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test RandomRestaurantSelectionStrategy
 */
class RandomRestaurantSelectionStrategyTest {

    private RandomRestaurantSelectionStrategy restaurantSelectionStrategy;

    @BeforeEach
    void setUp() {
        restaurantSelectionStrategy = new RandomRestaurantSelectionStrategy();
    }

    @AfterEach
    void tearDown() {
        restaurantSelectionStrategy = null;
    }

    /**
     * Given one restaurant, when pickRestaurant, return the only available choice
     */
    @Test
    void givenOneRestaurant_whenPickRestaurant_returnOnlyResult() {
        // given
        final Restaurant restaurant = new Restaurant(5L, 2L, "McFreddie Burgers",
                "Very nice burgers", RestaurantStatus.ACTIVE.name());

        // when
        final Restaurant result = restaurantSelectionStrategy.pickRestaurant(List.of(restaurant));

        // then
        assertNotNull(result);
        assertEquals(5L, result.id());
    }

    /**
     * Given multiple restaurant, when pickRestaurant, return random choice
     */
    @Test
    void givenMultipleRestaurants_whenPickRestaurant_returnRandom() {
        // given
        final Restaurant restaurant1 = new Restaurant(5L, 2L, "McFreddie Burgers",
                "Very nice burgers", RestaurantStatus.ACTIVE.name());
        final Restaurant restaurant2 = new Restaurant(15L, 2L, "OutThere Steaks",
                "Steaks on cheap!", RestaurantStatus.ACTIVE.name());
        final Restaurant restaurant3 = new Restaurant(38L, 2L, "Today's Fusion Food",
                "Fusion food", RestaurantStatus.ACTIVE.name());
        // when
        final Restaurant result = restaurantSelectionStrategy.pickRestaurant(List.of(restaurant1, restaurant2, restaurant3));

        // then
        assertNotNull(result);
        assertTrue(Set.of(5L, 15L, 38L).contains(result.id()));
    }
}