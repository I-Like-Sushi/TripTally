package org.example.triptally;

import org.example.triptally.user.*;
import org.example.triptally.user.dto.UserRequestDto;
import org.example.triptally.user.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    // NOT A UNIT TEST //
    // Unit tests are done to test the @service layer, not the @controller layer.

    @Mock private UserRepository userRepository;
    @Mock private UserService userService;
    @Mock private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser() {

        // Arrange

        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("UsernameTest");

        User user = new User();
        user.setUsername("UsernameTest");
        user.setId(1L);

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setUsername("UsernameTest");

        when(userService.createUser(requestDto)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(responseDto);

        // Act

        ResponseEntity<UserResponseDto> response = userController.createUser(requestDto);

        // Assert

        assertEquals(201, response.getStatusCode().value());
        assertEquals(URI.create("/users/1"), response.getHeaders().getLocation());
        assertSame(responseDto, response.getBody());
        verify(userService).createUser(requestDto);
    }

}

