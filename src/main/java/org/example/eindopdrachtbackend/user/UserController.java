package org.example.eindopdrachtbackend.user;

import org.example.eindopdrachtbackend.auth.AuthValidationService;
import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
import org.example.eindopdrachtbackend.user.dto.UserRequestDto;
import org.example.eindopdrachtbackend.user.dto.UserResponseDto;
import org.example.eindopdrachtbackend.user.dto.UserUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateDto dto,
            Authentication auth) {

        // Only self-update allowed
        authValidationService.validateSelfOrThrow(id, auth);

        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userMapper.updateEntityFromDto(dto, user);
        userRepo.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, Authentication auth) {
        authValidationService.validateSelfOrThrow(id, auth);
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        String response = "User " + user.getUsername() + " has successfully been deleted.";
        userRepo.delete(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/viewing-access/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponseDto> loggedInView(
            @PathVariable Long id, Authentication auth, @RequestParam Long loggedInUserId) {

        authValidationService.validateSelfOrThrow(loggedInUserId, auth);

        User loggedInUser = userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        User targetUser = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean hasViewingPermission = targetUser.getAllowedAccesView()
                .contains(loggedInUser.getUsername());

        return hasViewingPermission
                ? ResponseEntity.ok(userMapper.allowedAccesView(targetUser))
                : ResponseEntity.ok(userMapper.restrictedView(targetUser));
    }

    @PostMapping("/viewing-access/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> giveUserViewingPermission(
            @PathVariable Long id, Authentication auth, @RequestParam Long loggedInUserId) {

        authValidationService.validateSelfOrThrow(loggedInUserId, auth);
        User targetUser = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean granted = userService.grantViewingAccess(auth, targetUser);

        return granted
                ? ResponseEntity.ok("Both now have viewing access to each other.")
                : ResponseEntity.ok("You already had viewing access to " + targetUser.getUsername());
    }

    @DeleteMapping("/viewing-access/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteUserFromViewingAccess(
            @PathVariable Long id, Authentication auth, @RequestParam Long loggedInUserId) {

        authValidationService.validateSelfOrThrow(loggedInUserId, auth);
        User targetUser = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean removed = userService.removeViewerAccess(auth, targetUser);

        return removed
                ? ResponseEntity.ok("Both of your viewing accesses have been removed")
                : ResponseEntity.ok("You did not have viewing access to " + targetUser.getUsername());
    }






}
