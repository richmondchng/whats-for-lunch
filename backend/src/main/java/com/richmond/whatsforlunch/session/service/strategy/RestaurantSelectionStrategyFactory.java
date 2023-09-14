package com.richmond.whatsforlunch.session.service.strategy;

import com.richmond.whatsforlunch.session.util.ApplicationMessages;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory to manage the selection strategies.
 */
@Component
public final class RestaurantSelectionStrategyFactory {

    private final Map<String, RestaurantSelectionStrategy> strategies;

    public RestaurantSelectionStrategyFactory(final List<RestaurantSelectionStrategy> strategyList) {
        this.strategies = strategyList.stream().collect(Collectors.toUnmodifiableMap(
                RestaurantSelectionStrategy::getName, Function.identity()));
    }

    /**
     * Get strategy by name. Exception if not found
     * @param name stategy name
     * @return RestaurantSelectionStrategy
     */
    public RestaurantSelectionStrategy getStrategy(final String name) {
        final RestaurantSelectionStrategy result = this.strategies.get(name);
        if(result == null) {
            throw new IllegalArgumentException(ApplicationMessages.ERROR_STRATEGY_NOT_FOUND);
        }
        return result;
    }
}
