package org.example.triptally.admin.superadmin;

import jakarta.validation.Valid;
import org.example.triptally.admin.AdminModificationPolicy;
import org.example.triptally.admin.AdminService;
import org.example.triptally.auth.AuthValidationService;
import org.example.triptally.exception.user.UserNotSuperAdmin;
import org.example.triptally.exception.user.UserNotFoundException;
import org.example.triptally.user.User;
import org.example.triptally.user.UserMapper;
import org.example.triptally.user.UserRepository;
import org.example.triptally.user.dto.UserRequestDto;
import org.example.triptally.user.dto.UserResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/superadmin")
public class SuperAdminController {

    private final SuperAdminService superAdminService;
    private final UserMapper userMapper;
    private final AuthValidationService authValidationService;
    private final UserRepository userRepository;
    private final AdminService adminService;
    private final AdminModificationPolicy adminModificationPolicy;

    private final Logger log = LoggerFactory.getLogger(SuperAdminController.class);

    @Value("${SUPERADMIN_SECRET}")
    private String superAdminSecret;

    public SuperAdminController(SuperAdminService superAdminService, UserMapper userMapper, AuthValidationService authValidationService, UserRepository userRepository, AdminService adminService, AdminModificationPolicy adminModificationPolicy) {
        this.superAdminService = superAdminService;
        this.userMapper = userMapper;
        this.authValidationService = authValidationService;
        this.userRepository = userRepository;
        this.adminService = adminService;
        this.adminModificationPolicy = adminModificationPolicy;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createSuperAdmin(
            @RequestHeader("X-SUPERADMIN-SECRET") String providedSecret,
            @Valid @RequestBody UserRequestDto dto) {

        if (!providedSecret.equals(superAdminSecret)) {
            throw new UserNotSuperAdmin("Invalid SuperAdmin Password");
        }

        User newUser = superAdminService.createSuperAdmin(dto);
        UserResponseDto responseDto = userMapper.toDto(newUser);
        URI location = URI.create("/internal/superadmin-ops-9f3x7k/" + newUser.getId());
        return ResponseEntity.created(location).body(responseDto);
    }

    @PostMapping("/{superAdminId}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<UserResponseDto> createAdmin(@Valid @RequestBody UserRequestDto dto, Authentication auth, @PathVariable Long superAdminId) {

        authValidationService.validateSelfOrThrow(superAdminId, auth);

        User newUser = adminService.createAdmin(dto);
        UserResponseDto responseDto = userMapper.toDto(newUser);
        URI location = URI.create("/internal/superadmin-ops-9f3x7k/" + newUser.getId());
        return ResponseEntity.created(location).body(responseDto);
    }

    @DeleteMapping("/{superAdminId}/users/{targetId}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long targetId, Authentication auth, @PathVariable Long superAdminId, @RequestHeader(value = "X-SUPERADMIN-SECRET", required = false) String providedSecret) {
        authValidationService.validateSelfOrThrow(superAdminId, auth);

        User user = userRepository.findById(targetId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!superAdminSecret.equals(providedSecret)) {
            adminModificationPolicy.enforce(auth, targetId);
            return ResponseEntity.ok("Admin has been deleted");
        }

        userRepository.deleteById(user.getId());
        return ResponseEntity.ok("Superadmin override used by " + auth.getName() + " to delete user " + user.getUsername() + " with id " + user.getId());
    }

    @GetMapping("/{superAdminId}/users")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public List<User> fetchAllUsers(Authentication auth, @PathVariable Long superAdminId) {
        authValidationService.validateSelfOrThrow(superAdminId, auth);
        return userRepository.findAll();

    }

    @GetMapping("/{superAdminId}/users/{targetId}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<User> fetchUser(@PathVariable Long targetId, Authentication auth, @PathVariable Long superAdminId) {
        authValidationService.validateSelfOrThrow(superAdminId, auth);

        User user = userRepository.findById(targetId).orElseThrow(() -> new UserNotFoundException("User not found"));


        return ResponseEntity.ok(user);

    }

    @PutMapping("/{superAdminId}/users/{targetId}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<String> updateUser(
            @PathVariable Long targetId,
            @Valid @RequestBody User user,
            Authentication auth,
            @PathVariable Long superAdminId,
            @RequestHeader(value = "X-SUPERADMIN-SECRET", required = false) String providedSecret) {

        authValidationService.validateSelfOrThrow(superAdminId, auth);

        // If no override, enforce normal policy
        if (!superAdminSecret.equals(providedSecret)) {
            adminModificationPolicy.enforce(auth, targetId);
        } else {
            log.warn("Superadmin override used by {} to update user {}", auth.getName(), targetId);
        }

        user.setId(targetId);

        userRepository.save(user);

        if (superAdminSecret.equals(providedSecret)) {
            return ResponseEntity.ok("Superadmin override used by " + auth.getName() +
                    " to update user " + user.getUsername() + " with id " + user.getId());
        } else {
            return ResponseEntity.ok("User has been updated");
        }
    }

}
