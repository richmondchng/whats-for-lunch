package com.richmond.whatsforlunch.services;

import com.richmond.whatsforlunch.respository.UserRepository;
import com.richmond.whatsforlunch.respository.entity.UserEntity;
import com.richmond.whatsforlunch.services.dto.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Unit test UserService.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @AfterEach
    void tearDown() {
        userService = null;
    }

    /**
     * Given no existing users, when getAllUsers, return empty collection.
     */
    @Test
    void givenNoUsers_whenGetAllUsers_returnEmpty() {
        // when
        final Collection<User> results = userService.getAllUsers();

        // then
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    /**
     * Given existing users, when getAllUsers, return collection of users.
     */
    @Test
    void givenUsers_whenGetAllUsers_returnMappedUserBeans() {
        // given
        when(userRepository.findAll()).thenReturn(List.of(
                UserEntity.builder().id(100L).userName("jw").firstName("John").lastName("Wong").build()));

        // when
        final Collection<User> results = userService.getAllUsers();

        // then
        assertNotNull(results);
        assertEquals(1, results.size());

        final Iterator<User> itr = results.iterator();
        final User result = itr.next();
        assertEquals(100L, result.id());
        assertEquals("jw", result.userName());
        assertEquals("John", result.firstName());
        assertEquals("Wong", result.lastName());
        assertFalse(itr.hasNext());
    }
}