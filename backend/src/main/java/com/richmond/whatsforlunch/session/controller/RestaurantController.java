package com.richmond.whatsforlunch.session.controller;

import com.richmond.whatsforlunch.common.controller.StandardResponse;
import com.richmond.whatsforlunch.session.service.RestaurantService;
import com.richmond.whatsforlunch.session.util.ApplicationMessages;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to manage restaurant.
 */
@RestController
@RequestMapping("/api/v1/sessions/{sessionId}/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    /**
     * Add a new restaurant to session.
     * @param body request body
     * @return action status
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<ResponseAddRestaurant>> addRestaurant(@PathVariable final long sessionId,
                                                                                 @RequestBody RequestAddRestaurant body) {
        Assert.isTrue(sessionId > 0, ApplicationMessages.ERROR_SESSION_ID_MANDATORY);
        Assert.isTrue(body.userId() > 0, ApplicationMessages.ERROR_USER_ID_IS_MANDATORY);
        Assert.isTrue(StringUtils.isNotBlank(body.restaurant()), ApplicationMessages.ERROR_RESTAURANT_IS_MANDATORY);

        restaurantService.addRestaurantToSession(sessionId, body.userId(), body.restaurant(), body.description());

        return ResponseEntity.ok(new StandardResponse<>(new ResponseAddRestaurant(ApplicationMessages.SUCCESS_MESSAGE)));
    }
}

record RequestAddRestaurant(long userId, String restaurant, String description) {}

record ResponseAddRestaurant(String status) {}