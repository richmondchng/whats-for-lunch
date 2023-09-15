package com.richmond.whatsforlunch.users.service;


import com.richmond.whatsforlunch.users.repository.UserRepository;
import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import com.richmond.whatsforlunch.users.service.dto.SecurityUserDetails;
import com.richmond.whatsforlunch.users.service.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * User details service for security
 */
@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username)
                .map(this::mapToBean)
                .map(SecurityUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User is not valid"));
    }

    private User mapToBean(final UserEntity entity) {
        return new User(entity.getId(), entity.getUserName(), entity.getPassword(),
                Arrays.stream(entity.getRoles().split(",")).toList(),
                entity.getFirstName(), entity.getLastName());
    }
}
