package com.richmond.whatsforlunch.users.controller;

import com.richmond.whatsforlunch.common.controller.StandardResponse;
import com.richmond.whatsforlunch.users.service.UserService;
import com.richmond.whatsforlunch.users.service.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/currentuser")
@RequiredArgsConstructor
public class CurrentUserController {
    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<ResponseUser>> getCurrentUserByUserName(final Principal principal) {
        return ResponseEntity.ok(new StandardResponse<>(mapToBean(userService.getUserByUserName(principal.getName()))));
    }

    private ResponseUser mapToBean(final User bean) {
        return new ResponseUser(bean.id(), bean.userName(), bean.firstName(), bean.lastName());
    }
}
