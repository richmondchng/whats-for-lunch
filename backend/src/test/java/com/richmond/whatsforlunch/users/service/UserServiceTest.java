package com.richmond.whatsforlunch.users.service;

import com.richmond.whatsforlunch.users.repository.UserRepository;
import com.richmond.whatsforlunch.users.repository.entity.UserEntity;
import com.richmond.whatsforlunch.users.service.dto.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Comparator;
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
        final Collection<User> results = userService.getAllUsers(true);

        // then
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    /**
     * Given existing users excluding admin, when getAllUsers, return collection of users.
     */
    @Test
    void givenUsersExcludeAdmin_whenGetAllUsers_returnMappedUserBeansExceptAdmin() {
        // given
        when(userRepository.findAll()).thenReturn(List.of(
                UserEntity.builder().id(100L).userName("jw").firstName("John").lastName("Wong").build(),
                UserEntity.builder().id(101L).userName("admin").firstName("Admin").lastName("Admin").build(),
                UserEntity.builder().id(102L).userName("ron").firstName("Ronald").lastName("Lau").build()
        ));

        // when
        final Collection<User> results = userService.getAllUsers(false);

        // then
        assertNotNull(results);
        assertEquals(2, results.size());

        final Iterator<User> itr = results.stream().sorted(Comparator.comparing(User::id)).iterator();
        final User result1 = itr.next();
        assertEquals(100L, result1.id());
        assertEquals("jw", result1.userName());

        final User result2 = itr.next();
        assertEquals(102L, result2.id());
        assertEquals("ron", result2.userName());

        assertFalse(itr.hasNext());
    }

    /**
     * Given existing users including admin, when getAllUsers, return collection of users.
     */
    @Test
    void givenUsersIncludeAdmin_whenGetAllUsers_returnMappedUserBeansExceptAdmin() {
        // given
        when(userRepository.findAll()).thenReturn(List.of(
                UserEntity.builder().id(100L).userName("jw").firstName("John").lastName("Wong").build(),
                UserEntity.builder().id(101L).userName("admin").firstName("Admin").lastName("Admin").build(),
                UserEntity.builder().id(102L).userName("ron").firstName("Ronald").lastName("Lau").build()
        ));

        // when
        final Collection<User> results = userService.getAllUsers(true);

        // then
        assertNotNull(results);
        assertEquals(3, results.size());

        final Iterator<User> itr = results.stream().sorted(Comparator.comparing(User::id)).iterator();
        final User result1 = itr.next();
        assertEquals(100L, result1.id());
        assertEquals("jw", result1.userName());

        final User result2 = itr.next();
        assertEquals(101L, result2.id());
        assertEquals("admin", result2.userName());

        final User result3 = itr.next();
        assertEquals(102L, result3.id());
        assertEquals("ron", result3.userName());

        assertFalse(itr.hasNext());
    }
}