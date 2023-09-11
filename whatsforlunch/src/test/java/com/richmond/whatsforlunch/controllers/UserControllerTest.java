package com.richmond.whatsforlunch.controllers;

import com.richmond.whatsforlunch.services.UserService;
import com.richmond.whatsforlunch.services.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test UserController
 */
@WebMvcTest(controllers = { UserController.class })
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
                .andExpect(content().string("[]"));

        verify(userService, times(1)).getAllUsers();
    }

    /**
     * Given users exists, when invoke GET /api/v1/users, return array of users
     * @throws Exception exception
     */
    @Test
    void givenUsers_whenGetUsers_returnArrayWithUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(
                new User(1, "adamt", "adam", "tan"),
                new User(2, "johno", "john", "ong")));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userName", is("adamt")))
                .andExpect(jsonPath("$[0].firstName", is("adam")))
                .andExpect(jsonPath("$[0].lastName", is("tan")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].userName", is("johno")))
                .andExpect(jsonPath("$[1].firstName", is("john")))
                .andExpect(jsonPath("$[1].lastName", is("ong")));

        verify(userService, times(1)).getAllUsers();
    }
}