package com.richmond.whatsforlunch.controller;

import com.richmond.whatsforlunch.service.UserService;
import com.richmond.whatsforlunch.service.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * User controller.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<User>> getUsers() {
        return ResponseEntity.ok(new StandardResponse<>(List.copyOf(userService.getAllUsers())));
    }
}

