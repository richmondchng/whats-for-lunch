package com.richmond.whatsforlunch.users.controller;

import com.richmond.whatsforlunch.common.config.TestSecurityConfig;
import com.richmond.whatsforlunch.users.service.UserService;
import com.richmond.whatsforlunch.users.service.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test UserController
 */
@WebMvcTest(controllers = { UserController.class })
@Import({ TestSecurityConfig.class })
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    /**
     * Given no users, when invoke GET /api/v1/users, return empty array
     * @throws Exception exception
     */
    @Test
    void givenNoUsers_whenGetUsers_returnEmptyArray() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(0)));

        verify(userService, times(1)).getAllUsers();
    }

    /**
     * Given users exists, when invoke GET /api/v1/users, return array of users
     * @throws Exception exception
     */
    @Test
    void givenUsers_whenGetUsers_returnArrayWithUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(
                new User(1, "adamt", null, null, "adam", "tan"),
                new User(2, "johno", null, null, "john", "ong")));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].userName", is("adamt")))
                .andExpect(jsonPath("$.data[0].firstName", is("adam")))
                .andExpect(jsonPath("$.data[0].lastName", is("tan")))
                .andExpect(jsonPath("$.data[1].id", is(2)))
                .andExpect(jsonPath("$.data[1].userName", is("johno")))
                .andExpect(jsonPath("$.data[1].firstName", is("john")))
                .andExpect(jsonPath("$.data[1].lastName", is("ong")));

        verify(userService, times(1)).getAllUsers();
    }
}