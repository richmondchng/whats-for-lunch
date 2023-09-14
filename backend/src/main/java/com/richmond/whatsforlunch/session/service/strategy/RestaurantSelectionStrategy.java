package com.richmond.whatsforlunch.session.service.strategy;

import com.richmond.whatsforlunch.session.service.dto.Restaurant;

import java.util.List;

/**
 * Restaurant selection strategy.
 */
public interface RestaurantSelectionStrategy {

    String getName();

    /**
     * Pick a restaurant from the list of restaurants.
     * @param restaurantList list of restaurants
     * @return selected restaurant
     */
    Restaurant pickRestaurant(final List<Restaurant> restaurantList);
}
