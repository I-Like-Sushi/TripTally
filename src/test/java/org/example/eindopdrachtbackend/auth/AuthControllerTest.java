// AI example. Do not use.


//package org.example.eindopdrachtbackend.auth;
//
//import org.example.eindopdrachtbackend.auth.dto.LoginRequestDto;
//import org.example.eindopdrachtbackend.auth.dto.LoginResponseDto;
//import org.example.eindopdrachtbackend.exception.auth.InvalidLoginException;
//import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
//import org.example.eindopdrachtbackend.security.JwtService;
//import org.example.eindopdrachtbackend.travel.mapper.TripMapper;
//import org.example.eindopdrachtbackend.travel.dto.trip.TripResponseDto;
//import org.example.eindopdrachtbackend.user.User;
//import org.example.eindopdrachtbackend.user.UserMapper;
//import org.example.eindopdrachtbackend.user.UserRepository;
//import org.example.eindopdrachtbackend.user.dto.UserResponseDto;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class AuthControllerTest {
//
//    @Mock JwtService jwtService;
//    @Mock AuthenticationManager authenticationManager;
//    @Mock UserMapper userMapper;
//    @Mock UserRepository userRepository;
//    @Mock TripMapper tripMapper;
//
//    @InjectMocks
//    private AuthController controller;
//
//    @Test
//    void login_successful_returnsToken() {
//        // Arrange
//        LoginRequestDto request = new LoginRequestDto("alice", "password123");
//        Authentication auth = mock(Authentication.class);
//
//        when(authenticationManager.authenticate(
//                any(UsernamePasswordAuthenticationToken.class)))
//                .thenReturn(auth);
//        when(auth.getName()).thenReturn("alice");
//        when(jwtService.generateToken("alice")).thenReturn("jwt-token-xyz");
//
//        // Act
//        ResponseEntity<LoginResponseDto> response = controller.login(request);
//
//        // Assert
//        assertEquals(200, response.getStatusCodeValue());
//        LoginResponseDto body = response.getBody();
//        assertNotNull(body);
//        assertEquals("Login successful", body.getMessage());
//        assertEquals("jwt-token-xyz", body.getToken());
//
//        // Verify that Spring Security context is set
//        verify(authenticationManager).authenticate(any());
//    }
//
//    @Test
//    void login_badCredentials_throwsInvalidLoginException() {
//        // Arrange
//        when(authenticationManager.authenticate(any()))
//                .thenThrow(new BadCredentialsException("Bad creds"));
//
//        // Act & Assert
//        assertThrows(
//                InvalidLoginException.class,
//                () -> controller.login(new LoginRequestDto("bob", "wrong")));
//    }
//
//    @Test
//    void getCurrentUser_unauthenticated_returns401() {
//        // Act
//        ResponseEntity<?> response = controller.getCurrentUser(null);
//
//        // Assert
//        assertEquals(401, response.getStatusCodeValue());
//        assertEquals("Unauthorized", response.getBody());
//    }
//
//    @Test
//    void getCurrentUser_userNotFound_throwsUserNotFoundException() {
//        // Arrange
//        Authentication auth = mock(Authentication.class);
//        when(auth.isAuthenticated()).thenReturn(true);
//        when(auth.getName()).thenReturn("ghost");
//        when(userRepository.findByUsername("ghost"))
//                .thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(
//                UserNotFoundException.class,
//                () -> controller.getCurrentUser(auth));
//    }
//
//    @Test
//    void getCurrentUser_authenticated_returnsUserDto() {
//        // Arrange
//        Authentication auth = mock(Authentication.class);
//        when(auth.isAuthenticated()).thenReturn(true);
//        when(auth.getName()).thenReturn("jdoe");
//
//        // Build a User with one trip and one allowedAccess
//        User user = new User();
//        user.addAllowedAccesView("friend1");
//        // Assuming Trip has a no-arg ctor and setter for id
//        var tripEntity = new org.example.eindopdrachtbackend.travel.model.Trip();
//        tripEntity.setTripId("TRIP123");
//        user.addTrip(tripEntity);
//
//        when(userRepository.findByUsername("jdoe"))
//                .thenReturn(Optional.of(user));
//
//        // Stub mappers
//        UserResponseDto userDto = new UserResponseDto();
//        when(userMapper.toDto(user)).thenReturn(userDto);
//
//        TripResponseDto tripDto = new TripResponseDto();
//        when(tripMapper.toDto(tripEntity)).thenReturn(tripDto);
//
//        // Act
//        ResponseEntity<?> response = controller.getCurrentUser(auth);
//
//        // Assert
//        assertEquals(200, response.getStatusCodeValue());
//        assertSame(userDto, response.getBody());
//
//        // Verify DTO was enriched correctly
//        assertEquals(List.of("friend1"), userDto.getAllowViewingAccesTo());
//        assertEquals(List.of(tripDto), userDto.getTrips());
//    }
//}
