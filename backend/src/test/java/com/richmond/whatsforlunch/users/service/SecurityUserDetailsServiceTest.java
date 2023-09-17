package com.richmond.whatsforlunch.users.service;

import com.richmond.whatsforlunch.users.repository.UserRepository;
import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import com.richmond.whatsforlunch.users.service.dto.SecurityUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test SecurityUserDetailsService
 */
@ExtendWith(MockitoExtension.class)
class SecurityUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private SecurityUserDetailsService securityUserDetailsService;

    @BeforeEach
    void setUp() {
        securityUserDetailsService = new SecurityUserDetailsService(userRepository);
    }

    @AfterEach
    void tearDown() {
        securityUserDetailsService = null;
    }

    /**
     * Given user is valid, when loadUserByUsername, return user
     */
    @Test
    void givenUsernameIsValid_whenLoadUserByUsername_returnUser() {
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(
                UserEntity.builder().userName("edward").password("password").roles("ROLE_USER").build()
        ));

        final UserDetails result = securityUserDetailsService.loadUserByUsername("edward");

        verify(userRepository, times(1)).findByUserName(eq("edward"));
        assertTrue(result instanceof SecurityUserDetails);

        assertEquals("edward", result.getUsername());
        assertEquals("password", result.getPassword());
        assertEquals(1, result.getAuthorities().size());
        assertEquals("ROLE_USER", result.getAuthorities().iterator().next().getAuthority());
    }

    /**
     * Given username is not valid, when loadUserByUsername, return user
     */
    @Test
    void givenUsernameIsNotValid_whenLoadUserByUsername_throwException() {
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());

        try {
            securityUserDetailsService.loadUserByUsername("edward");
            fail("Expect exception to be thrown");
        } catch(UsernameNotFoundException e) {
            assertEquals("User is not valid", e.getMessage());
        }
        verify(userRepository, times(1)).findByUserName(eq("edward"));
    }
}