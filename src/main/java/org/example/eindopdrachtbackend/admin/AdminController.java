package org.example.eindopdrachtbackend.admin;

import org.example.eindopdrachtbackend.auth.AuthValidationService;
import org.example.eindopdrachtbackend.exception.auth.UserNotSuperAdmin;
import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
import org.example.eindopdrachtbackend.user.User;
import org.example.eindopdrachtbackend.user.UserMapper;
import org.example.eindopdrachtbackend.user.UserRepo;
import org.example.eindopdrachtbackend.user.dto.UserResponseDto;
import org.example.eindopdrachtbackend.user.dto.UserUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepo userRepo;
    private final AuthValidationService authValidationService;
    private final AdminModificationPolicy adminModificationPolicy;
    private final UserMapper userMapper;

    public AdminController(UserRepo userRepo, AuthValidationService authValidationService, AdminModificationPolicy adminModificationPolicy, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.authValidationService = authValidationService;
        this.adminModificationPolicy = adminModificationPolicy;
        this.userMapper = userMapper;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id, @RequestParam long adminId, Authentication auth) {
        authValidationService.validateSelfOrThrow(adminId, auth);
        adminModificationPolicy.enforce(auth, adminId);

        String username = auth.getName();
        userRepo.deleteById(id);
        return ResponseEntity.ok("Account of " + username + " deleted successfully");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getAccount(@PathVariable Long id, Authentication auth, @RequestParam long adminId) {
        authValidationService.validateSelfOrThrow(adminId, auth);

        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User unknown"));

        UserResponseDto dto = userMapper.toDto(user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/fetchAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(Authentication auth, @RequestParam long adminId) {
        authValidationService.validateSelfOrThrow(adminId, auth);
        List<UserResponseDto> AllAccountsToDto = userRepo.findAll().stream()
                .map(userMapper::toDto)
                .toList();

        return ResponseEntity.ok(AllAccountsToDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateAccount(
            @PathVariable Long id,
            @RequestBody UserUpdateDto dto,
            Authentication auth) {

        adminModificationPolicy.enforce(auth, id);

        User targetUser = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userMapper.updateEntityFromDto(dto, targetUser);
        userRepo.save(targetUser);

        return ResponseEntity.ok(userMapper.toDto(targetUser));
    }



}
