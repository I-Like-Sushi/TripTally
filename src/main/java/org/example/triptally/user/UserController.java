package org.example.triptally.user;

import jakarta.validation.Valid;
import org.example.triptally.auth.AuthValidationService;
import org.example.triptally.exception.user.UserNotFoundException;
import org.example.triptally.user.dto.UserRequestDto;
import org.example.triptally.user.dto.UserResponseDto;
import org.example.triptally.user.dto.UserUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthValidationService authValidationService;

    public UserController(UserService userService, UserRepository userRepository, UserMapper userMapper, AuthValidationService authValidationService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authValidationService = authValidationService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto dto) {
        User newUser = userService.createUser(dto);
        UserResponseDto responseDto = userMapper.toDto(newUser);
        URI location = URI.create("/users/" + newUser.getId());
        return ResponseEntity.created(location).body(responseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateDto dto,
            Authentication auth) {

        // Only self-update allowed
        authValidationService.validateSelfOrThrow(id, auth);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userMapper.updateEntityFromDto(dto, user);
        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, Authentication auth) {
        authValidationService.validateSelfOrThrow(id, auth);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        String response = "User " + user.getUsername() + " has successfully been deleted.";
        userRepository.delete(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/viewing-access")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponseDto> loggedInView(
            @PathVariable Long userId, Authentication auth, @RequestParam Long loggedInUserId) {

        authValidationService.validateSelfOrThrow(loggedInUserId, auth);

        boolean hasViewingPermission = userService.hasViewingAccess(auth, userId);

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return hasViewingPermission
                ? ResponseEntity.ok(userMapper.allowedAccesView(targetUser))
                : ResponseEntity.ok(userMapper.restrictedView(targetUser));
    }

    @PostMapping("/{userId}/viewing-access")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> giveUserViewingPermission(
            @PathVariable Long userId, Authentication auth, @RequestParam Long loggedInUserId) {

        authValidationService.validateSelfOrThrow(loggedInUserId, auth);
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean granted = userService.grantViewingAccess(auth, targetUser);

        return granted
                ? ResponseEntity.ok("Both now have viewing access to each other.")
                : ResponseEntity.ok("You already had viewing access to " + targetUser.getUsername());
    }

    @DeleteMapping("/{userId}/viewing-access/{targetUserId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteUserFromViewingAccess(
            @PathVariable Long userId, Authentication auth, @PathVariable Long targetUserId) {

        authValidationService.validateSelfOrThrow(userId, auth);
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean removed = userService.removeViewerAccess(auth, targetUser);

        return removed
                ? ResponseEntity.ok("Both of your viewing accesses have been removed")
                : ResponseEntity.ok("You did not have viewing access to " + targetUser.getUsername());
    }




}
