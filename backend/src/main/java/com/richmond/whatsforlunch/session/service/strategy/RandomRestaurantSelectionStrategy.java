package com.richmond.whatsforlunch.session.service.strategy;

import com.richmond.whatsforlunch.session.service.dto.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * Generate a random number to select using array index.
 * This is injected into {@link RestaurantSelectionStrategyFactory}
 */
@Component
public class RandomRestaurantSelectionStrategy implements RestaurantSelectionStrategy{

    @Override
    public String getName() {
        return "RANDOM";
    }

    @Override
    public Restaurant pickRestaurant(final List<Restaurant> restaurantList) {
        final int lowerBound = 0;
        final int upperBound = restaurantList.size();
        Random random = new Random();
        final int randomNumber = random.nextInt(upperBound - lowerBound) + lowerBound;
        return restaurantList.get(randomNumber);
    }
}
