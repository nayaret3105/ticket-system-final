package com.tickets.msauth.service;

import com.tickets.msauth.dto.AuthResponseDto;
import com.tickets.msauth.dto.LoginRequestDto;
import com.tickets.msauth.dto.MessageResponseDto;
import com.tickets.msauth.dto.RegisterRequestDto;
import com.tickets.msauth.exception.InvalidCredentialsException;
import com.tickets.msauth.exception.UserAlreadyExistsException;
import com.tickets.msauth.model.User;
import com.tickets.msauth.repository.UserRepository;
import com.tickets.msauth.security.JwtUtil;
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
    void register_whenCredentialsAreUnique_shouldReturnSuccessMessage() {
        RegisterRequestDto dto = new RegisterRequestDto("testuser", "test@example.com", "password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        MessageResponseDto response = authService.register(dto);

        assertThat(response.message()).contains("testuser");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_whenUsernameAlreadyExists_shouldThrowUserAlreadyExistsException() {
        RegisterRequestDto dto = new RegisterRequestDto("existinguser", "test@example.com", "password123");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(dto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("existinguser");

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_whenEmailAlreadyExists_shouldThrowUserAlreadyExistsException() {
        RegisterRequestDto dto = new RegisterRequestDto("newuser", "existing@example.com", "password123");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(dto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("existing@example.com");

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_whenValidCredentials_shouldReturnAuthResponseWithToken() {
        LoginRequestDto dto = new LoginRequestDto("testuser", "password123");
        User user = buildUser();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPass")).thenReturn(true);
        when(jwtUtil.generateToken("testuser", "USER")).thenReturn("jwt-token");

        AuthResponseDto response = authService.login(dto);

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.username()).isEqualTo("testuser");
        assertThat(response.message()).isEqualTo("Login exitoso");
    }

    @Test
    void login_whenUserNotFound_shouldThrowInvalidCredentialsException() {
        LoginRequestDto dto = new LoginRequestDto("unknownuser", "password123");

        when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_whenPasswordDoesNotMatch_shouldThrowInvalidCredentialsException() {
        LoginRequestDto dto = new LoginRequestDto("testuser", "wrongpassword");
        User user = buildUser();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPass")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(dto))
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
