package org.example.triptally.admin;

import org.example.triptally.auth.AuthValidationService;
import org.example.triptally.exception.user.UserNotFoundException;
import org.example.triptally.user.User;
import org.example.triptally.user.UserMapper;
import org.example.triptally.user.UserRepository;
import org.example.triptally.user.dto.UserResponseDto;
import org.example.triptally.user.dto.UserUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final AuthValidationService authValidationService;
    private final AdminModificationPolicy adminModificationPolicy;
    private final UserMapper userMapper;

    public AdminController(UserRepository userRepository, AuthValidationService authValidationService, AdminModificationPolicy adminModificationPolicy, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.authValidationService = authValidationService;
        this.adminModificationPolicy = adminModificationPolicy;
        this.userMapper = userMapper;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id, @RequestParam Long adminId, Authentication auth) {
        authValidationService.validateSelfOrThrow(adminId, auth);
        adminModificationPolicy.enforce(auth, id);

        String username = auth.getName();
        userRepository.deleteById(id);
        return ResponseEntity.ok("Account of " + username + " deleted successfully");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getAccount(@PathVariable Long id, Authentication auth, @RequestParam Long adminId) {
        authValidationService.validateSelfOrThrow(adminId, auth);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User unknown"));

        UserResponseDto dto = userMapper.toDto(user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(Authentication auth, @RequestParam Long adminId) {
        authValidationService.validateSelfOrThrow(adminId, auth);
        List<UserResponseDto> AllAccountsToDto = userRepository.findAll().stream()
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

        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userMapper.updateEntityFromDto(dto, targetUser);
        userRepository.save(targetUser);

        return ResponseEntity.ok(userMapper.toDto(targetUser));
    }



}
