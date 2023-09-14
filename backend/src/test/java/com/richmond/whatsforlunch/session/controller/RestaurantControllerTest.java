package com.richmond.whatsforlunch.session.controller;

import com.richmond.whatsforlunch.session.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test RestaurantController
 */
@WebMvcTest(controllers = { RestaurantController.class })
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;


    /**
     * Given request body is valid, when invoke POST /api/v1/sessions/{sessionId}/restaurant, return success status
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
     * Given request body does not contain mandatory user Id field, when invoke POST /api/v1/sessions/{sessionId}/restaurant, fail and throw error
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
                .andExpect(jsonPath("$.message", is("User Id is mandatory")))
                .andExpect(jsonPath("$.path", is("/api/v1/sessions/2/restaurants")));

        verifyNoInteractions(restaurantService);
    }

    /**
     * Given request body does not contain mandatory restaurant field, when invoke POST /api/v1/sessions/{sessionId}/restaurant, fail and throw error
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
}