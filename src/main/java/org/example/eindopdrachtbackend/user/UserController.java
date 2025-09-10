package org.example.eindopdrachtbackend.user;

import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
import org.example.eindopdrachtbackend.user.dto.UserRequestDto;
import org.example.eindopdrachtbackend.user.dto.UserResponseDto;
import org.example.eindopdrachtbackend.user.dto.UserUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto dto) {
        User newUser = userService.createUser(dto);
        UserResponseDto responseDto = userMapper.toDto(newUser);
        System.out.println(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        UserResponseDto responseDto = userMapper.toDto(user);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto dto) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        userMapper.updateEntityFromDto(dto, user);
        userRepo.save(user);
        UserResponseDto responseDto = userMapper.toDto(user);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        String response = "User " + user.getUsername() + " has successfully been deleted. \n";
        userRepo.delete(user);
        return ResponseEntity.ok(response);
    }

}
