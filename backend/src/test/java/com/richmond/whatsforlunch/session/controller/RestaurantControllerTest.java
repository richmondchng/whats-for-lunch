package com.richmond.whatsforlunch.session.controller;

import com.richmond.whatsforlunch.common.config.TestSecurityConfig;
import com.richmond.whatsforlunch.session.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test RestaurantController
 */
@WebMvcTest(controllers = { RestaurantController.class })
@Import({ TestSecurityConfig.class })
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    /**
     * Given request body is valid, when invoke POST /api/v1/sessions/{sessionId}/restaurants, return success status
     * @throws Exception exception
     */
    @Test
    void givenRequestBodyIsValid_whenAddRestaurant_returnSuccessStatus() throws Exception {

        final String content = "{\"userId\":\"88\", \"restaurant\":\"Kenny Fried Chicken\", \"description\": \"Good value meals\"}";
        mockMvc.perform(post("/api/v1/sessions/2/restaurants").contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].status", is("Success")));

        verify(restaurantService, times(1)).addRestaurantToSession(2L, 88L,
                "Kenny Fried Chicken", "Good value meals");
    }

    /**
     * Given request body does not contain mandatory user Id field, when invoke POST /api/v1/sessions/{sessionId}/restaurants, fail and throw error
     * @throws Exception exception
     */
    @Test
    void givenRequestBodyWithoutUserId_whenAddRestaurant_returnError() throws Exception {

        final String content = "{\"restaurant\":\"Kenny Fried Chicken\", \"description\": \"Good value meals\"}";
        mockMvc.perform(post("/api/v1/sessions/2/restaurants").contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("User ID is mandatory")))
                .andExpect(jsonPath("$.path", is("/api/v1/sessions/2/restaurants")));

        verifyNoInteractions(restaurantService);
    }

    /**
     * Given request body does not contain mandatory restaurant field, when invoke POST /api/v1/sessions/{sessionId}/restaurants, fail and throw error
     * @throws Exception exception
     */
    @Test
    void givenRequestBodyWithoutRestaurant_whenAddRestaurant_returnError() throws Exception {

        final String content = "{\"userId\":\"88\"}";
        mockMvc.perform(post("/api/v1/sessions/2/restaurants").contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Restaurant is mandatory")))
                .andExpect(jsonPath("$.path", is("/api/v1/sessions/2/restaurants")));

        verifyNoInteractions(restaurantService);
    }

    /**
     * Given request is valid, when invoke DELETE /api/v1/sessions/{sessionId}/restaurants/{restaurantId}, return success status
     * @throws Exception exception
     */
    @Test
    void givenRequestBodyIsValid_whenDeleteRestaurant_returnSuccessStatus() throws Exception {

        mockMvc.perform(delete("/api/v1/sessions/2/restaurants/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].status", is("Success")));

        verify(restaurantService, times(1)).deleteRestaurantFromSession(2L, 5L);
    }

    /**
     * Given path session ID is zero, when invoke DELETE /api/v1/sessions/{sessionId}/restaurants/{restaurantId}, fail and throw error
     * @throws Exception exception
     */
    @Test
    void givenSessionIdIsZero_whenDeleteRestaurant_returnError() throws Exception {

        mockMvc.perform(delete("/api/v1/sessions/0/restaurants/5"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Session ID is mandatory")))
                .andExpect(jsonPath("$.path", is("/api/v1/sessions/0/restaurants/5")));

        verifyNoInteractions(restaurantService);
    }

    /**
     * Given path restaurant ID is zero, when invoke DELETE /api/v1/sessions/{sessionId}/restaurants/{restaurantId}, fail and throw error
     * @throws Exception exception
     */
    @Test
    void givenRestaurantIdIsZero_whenDeleteRestaurant_returnError() throws Exception {

        mockMvc.perform(delete("/api/v1/sessions/2/restaurants/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Restaurant ID is mandatory")))
                .andExpect(jsonPath("$.path", is("/api/v1/sessions/2/restaurants/0")));

        verifyNoInteractions(restaurantService);
    }
}