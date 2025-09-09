package org.example.eindopdrachtbackend.user;

import org.example.eindopdrachtbackend.user.dto.UserRequestDto;
import org.example.eindopdrachtbackend.user.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserRepo userRepo, UserMapper userMapper) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto dto) {
        User newUser = userService.createUser(dto);
        userRepo.save(newUser);
        UserResponseDto responseDto = userMapper.toDto(newUser);
        return ResponseEntity.ok(responseDto);

    }

}
