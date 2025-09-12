package org.example.eindopdrachtbackend.user;

import org.example.eindopdrachtbackend.auth.AuthValidationService;
import org.example.eindopdrachtbackend.exception.auth.UserNotSuperAdmin;
import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
import org.example.eindopdrachtbackend.user.dto.UserRequestDto;
import org.example.eindopdrachtbackend.user.dto.UserResponseDto;
import org.example.eindopdrachtbackend.user.dto.UserUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final AuthValidationService authValidationService;

    public UserController(UserService userService, UserRepo userRepo, UserMapper userMapper, AuthValidationService authValidationService) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.authValidationService = authValidationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto dto) {
        User newUser = userService.createUser(dto);
        UserResponseDto responseDto = userMapper.toDto(newUser);
        System.out.println(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateDto dto,
            Authentication auth) {

        // Only self-update allowed
        authValidationService.validateSelfOrThrow(id, auth);

        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // No role change possible because UserUpdateDto has no roles field
        userMapper.updateEntityFromDto(dto, user);
        userRepo.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, Authentication auth) {
        authValidationService.validateSelfOrThrow(id, auth);
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        String response = "User " + user.getUsername() + " has successfully been deleted.";
        userRepo.delete(user);
        return ResponseEntity.ok(response);
    }

    // ("/auth/me") will already handle the get requests for the logged in user.

}
