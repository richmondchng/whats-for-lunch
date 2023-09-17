package com.richmond.whatsforlunch.users.service.dto;

import java.util.List;

public record User(long id, String userName, String password, List<String> roles, String firstName, String lastName) {
}
