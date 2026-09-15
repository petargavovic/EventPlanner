package com.events.planner.service.impl;

import com.events.planner.dto.UserDto;
import com.events.planner.entity.User;
import com.events.planner.mapper.impl.UserDtoEntityMapper;
import com.events.planner.repository.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

public class UserServiceImplTest {

    private UserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeMethod
    public void setUp() {
        userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, new UserDtoEntityMapper(), passwordEncoder);
    }

    @Test
    public void shouldReturnUserById() throws Exception {
        User user = user();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getById(1L);

        assertEquals(result.getId(), Long.valueOf(1L));
        assertEquals(result.getEmail(), "user@example.com");
    }

    @Test
    public void shouldReturnUserByEmail() throws Exception {
        User user = user();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        UserDto result = userService.getByEmail("user@example.com");

        assertEquals(result.getId(), Long.valueOf(1L));
        assertEquals(result.getName(), "Petar");
    }

    @Test
    public void shouldReturnAllUsers() {
        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(user())));

        var result = userService.getAll(0, 10);

        assertEquals(result.getTotalElements(), 1L);
        assertEquals(result.getContent().get(0).getEmail(), "user@example.com");
    }

    @Test
    public void shouldDeleteExistingUser() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    public void shouldRejectDeletingMissingUser() {
        when(userRepository.existsById(99L)).thenReturn(false);

        NoSuchElementException exception = expectThrows(
                NoSuchElementException.class,
                () -> userService.delete(99L)
        );

        assertEquals(exception.getMessage(), "User not found.");
        verify(userRepository, never()).deleteById(99L);
    }

    private User user() {
        return new User(1L, "Petar", "Petrovic", "user@example.com", "encoded", false);
    }
}
