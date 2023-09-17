package com.richmond.whatsforlunch.users.controller;

import com.richmond.whatsforlunch.common.controller.StandardResponse;
import com.richmond.whatsforlunch.users.service.UserService;
import com.richmond.whatsforlunch.users.service.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<StandardResponse<ResponseUser>> getUsers(@RequestParam(defaultValue = "false") final boolean includeAdmin) {
        return ResponseEntity.ok(new StandardResponse<>(mapToBeans(userService.getAllUsers(includeAdmin))));
    }

    @GetMapping(value = "/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<ResponseUser>> getUserByUserName(@PathVariable final String userName) {
        return ResponseEntity.ok(new StandardResponse<>(mapToBean(userService.getUserByUserName(userName))));
    }

    private List<ResponseUser> mapToBeans(final List<User> beans) {
        return beans.stream().map(this::mapToBean).toList();
    }

    private ResponseUser mapToBean(final User bean) {
        return new ResponseUser(bean.id(), bean.userName(), bean.firstName(), bean.lastName());
    }
}

record ResponseUser (long id, String userName, String firstName, String lastName) { }

