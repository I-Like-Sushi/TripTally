package org.example.triptally.unit;

import org.example.triptally.user.User;
import org.example.triptally.user.UserMapper;
import org.example.triptally.user.UserRepository;
import org.example.triptally.user.UserService;
import org.example.triptally.user.dto.UserRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private Authentication auth;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_creates_and_saves_user() {
        // Arrange
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("UsernameTest");
        requestDto.setPassword("Testing_123");
        requestDto.setFirstName("Hans");
        requestDto.setLastName("Gerda");
        requestDto.setDateOfBirth(LocalDate.of(1990, 5, 20));
        requestDto.setEmail("Testing_123@live.nl");
        requestDto.setBio("This is my bio");
        requestDto.setGender("Male");

        User toSave = new User();
        toSave.setUsername("UsernameTest");

        when(userMapper.toEntity(requestDto)).thenReturn(toSave);
        when(passwordEncoder.encode("Testing_123")).thenReturn("encodedPassword");

        User saved = new User();
        saved.setUsername("UsernameTest");
        saved.setId(1L);
        saved.setPassword("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(saved);

        // Act
        User result = userService.createUser(requestDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("UsernameTest");
        assertThat(result.getPassword()).isEqualTo("encodedPassword");

        verify(userMapper).toEntity(requestDto);
        verify(passwordEncoder).encode("Testing_123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void hasViewingAccess_false_when_not_allowed_and_not_self() {
        // Arrange
        User loggedIn = new User();
        loggedIn.setId(100L);
        loggedIn.setUsername("jdoe");
        loggedIn.setAllowedAccesView(new ArrayList<>());

        User target = new User();
        target.setId(200L);
        target.setUsername("alice");
        target.setAllowedAccesView(new ArrayList<>());

        when(auth.getName()).thenReturn("jdoe");
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(loggedIn));
        when(userRepository.findById(200L)).thenReturn(Optional.of(target));

        // Act
        boolean allowed = userService.hasViewingAccess(auth, 200L);

        // Assert
        assertThat(allowed).isFalse();
        verify(userRepository).findByUsername("jdoe");
        verify(userRepository).findById(200L);
    }

    @Test
    void hasViewingAccess_true_when_target_allows_logged_in() {
        // Arrange
        User loggedIn = new User();
        loggedIn.setId(100L);
        loggedIn.setUsername("jdoe");
        loggedIn.setAllowedAccesView(new ArrayList<>());

        User target = new User();
        target.setId(200L);
        target.setUsername("alice");
        target.setAllowedAccesView(new ArrayList<>());
        target.getAllowedAccesView().add("jdoe");

        when(auth.getName()).thenReturn("jdoe");
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(loggedIn));
        when(userRepository.findById(200L)).thenReturn(Optional.of(target));

        // Act
        boolean allowed = userService.hasViewingAccess(auth, 200L);

        // Assert
        assertThat(allowed).isTrue();
    }

    @Test
    void hasViewingAccess_true_when_self() {
        // Arrange
        User loggedIn = new User();
        loggedIn.setId(100L);
        loggedIn.setUsername("jdoe");
        loggedIn.setAllowedAccesView(new ArrayList<>());

        when(auth.getName()).thenReturn("jdoe");
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(loggedIn));
        when(userRepository.findById(100L)).thenReturn(Optional.of(loggedIn));

        // Act
        boolean allowed = userService.hasViewingAccess(auth, 100L);

        // Assert
        assertThat(allowed).isTrue();
    }

    @Test
    void grantViewingAccess_adds_both_sides_and_saves_when_not_already_allowed() {
        // Arrange
        User loggedIn = new User();
        loggedIn.setId(100L);
        loggedIn.setUsername("jdoe");
        loggedIn.setAllowedAccesView(new ArrayList<>());

        User target = new User();
        target.setId(200L);
        target.setUsername("alice");
        target.setAllowedAccesView(new ArrayList<>());

        when(auth.getName()).thenReturn("jdoe");
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(loggedIn));

        // Act
        boolean changed = userService.grantViewingAccess(auth, target);

        // Assert
        assertThat(changed).isTrue();
        assertThat(target.getAllowedAccesView()).contains("jdoe");
        assertThat(loggedIn.getAllowedAccesView()).contains("alice");

        verify(userRepository).save(target);
        verify(userRepository).save(loggedIn);
    }

    @Test
    void grantViewingAccess_noop_when_already_allowed() {
        // Arrange
        User loggedIn = new User();
        loggedIn.setId(100L);
        loggedIn.setUsername("jdoe");
        loggedIn.setAllowedAccesView(new ArrayList<>());

        User target = new User();
        target.setId(200L);
        target.setUsername("alice");
        target.setAllowedAccesView(new ArrayList<>());
        target.getAllowedAccesView().add("jdoe");

        when(auth.getName()).thenReturn("jdoe");
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(loggedIn));

        // Act
        boolean changed = userService.grantViewingAccess(auth, target);

        // Assert
        assertThat(changed).isFalse();
        verify(userRepository, never()).save(any());
    }

    @Test
    void removeViewerAccess_removes_both_sides_and_saves_when_currently_allowed() {
        // Arrange
        User loggedIn = new User();
        loggedIn.setId(100L);
        loggedIn.setUsername("jdoe");
        loggedIn.setAllowedAccesView(new ArrayList<>());
        loggedIn.getAllowedAccesView().add("alice");

        User target = new User();
        target.setId(200L);
        target.setUsername("alice");
        target.setAllowedAccesView(new ArrayList<>());
        target.getAllowedAccesView().add("jdoe");

        when(auth.getName()).thenReturn("jdoe");
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(loggedIn));

        // Act
        boolean changed = userService.removeViewerAccess(auth, target);

        // Assert
        assertThat(changed).isTrue();
        assertThat(target.getAllowedAccesView()).doesNotContain("jdoe");
        assertThat(loggedIn.getAllowedAccesView()).doesNotContain("alice");

        verify(userRepository).save(target);
        verify(userRepository).save(loggedIn);
    }

    @Test
    void removeViewerAccess_noop_when_not_currently_allowed() {
        // Arrange
        User loggedIn = new User();
        loggedIn.setId(100L);
        loggedIn.setUsername("jdoe");
        loggedIn.setAllowedAccesView(new ArrayList<>());

        User target = new User();
        target.setId(200L);
        target.setUsername("alice");
        target.setAllowedAccesView(new ArrayList<>());

        when(auth.getName()).thenReturn("jdoe");
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(loggedIn));

        // Act
        boolean changed = userService.removeViewerAccess(auth, target);

        // Assert
        assertThat(changed).isFalse();
        verify(userRepository, never()).save(any());
    }

}
