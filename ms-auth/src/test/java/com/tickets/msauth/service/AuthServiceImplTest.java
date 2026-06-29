package com.tickets.msauth.service;

import com.tickets.msauth.dto.request.LoginRequest;
import com.tickets.msauth.dto.request.RegisterRequest;
import com.tickets.msauth.dto.response.AuthResponse;
import com.tickets.msauth.entity.User;
import com.tickets.msauth.exception.InvalidCredentialsException;
import com.tickets.msauth.exception.UserAlreadyExistsException;
import com.tickets.msauth.repository.UserRepository;
import com.tickets.msauth.security.JwtUtil;
import com.tickets.msauth.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private User buildUser() {
        return User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("encodedPass")
                .role("USER")
                .build();
    }

    @Test
    void register_whenCredentialsAreUnique_shouldReturnAuthResponseWithToken() {
        RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.username()).isEqualTo("testuser");
        assertThat(response.email()).isEqualTo("test@example.com");
        assertThat(response.role()).isEqualTo("USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_whenUsernameAlreadyExists_shouldThrowUserAlreadyExistsException() {
        RegisterRequest request = new RegisterRequest("existinguser", "test@example.com", "password123");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("existinguser");

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_whenEmailAlreadyExists_shouldThrowUserAlreadyExistsException() {
        RegisterRequest request = new RegisterRequest("newuser", "existing@example.com", "password123");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("existing@example.com");

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_whenValidCredentials_shouldReturnAuthResponseWithToken() {
        LoginRequest request = new LoginRequest("testuser", "password123");
        User user = buildUser();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPass")).thenReturn(true);
        when(jwtUtil.generateToken("testuser", "USER")).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.username()).isEqualTo("testuser");
        assertThat(response.message()).isEqualTo("Login exitoso");
    }

    @Test
    void login_whenUserNotFound_shouldThrowInvalidCredentialsException() {
        LoginRequest request = new LoginRequest("unknownuser", "password123");

        when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_whenPasswordDoesNotMatch_shouldThrowInvalidCredentialsException() {
        LoginRequest request = new LoginRequest("testuser", "wrongpassword");
        User user = buildUser();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPass")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void validateToken_whenTokenIsValid_shouldReturnTrue() {
        when(jwtUtil.validateToken("valid-token")).thenReturn(true);

        assertThat(authService.validateToken("valid-token")).isTrue();
    }

    @Test
    void validateToken_whenTokenIsInvalid_shouldReturnFalse() {
        when(jwtUtil.validateToken("invalid-token")).thenReturn(false);

        assertThat(authService.validateToken("invalid-token")).isFalse();
    }
}
