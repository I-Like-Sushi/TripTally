package org.example.eindopdrachtbackend.admin.superadmin;

import jakarta.servlet.http.HttpServletRequest;
import org.example.eindopdrachtbackend.admin.AdminService;
import org.example.eindopdrachtbackend.auth.AuthValidationService;
import org.example.eindopdrachtbackend.exception.auth.UserNotSuperAdmin;
import org.example.eindopdrachtbackend.user.User;
import org.example.eindopdrachtbackend.user.UserMapper;
import org.example.eindopdrachtbackend.user.UserRepo;
import org.example.eindopdrachtbackend.user.dto.UserRequestDto;
import org.example.eindopdrachtbackend.user.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/internal/superadmin-ops-9f3x7k")
public class SuperAdminController {

    private final SuperAdminService superAdminService;
    private final UserMapper userMapper;
    private final AuthValidationService authValidationService;
    private final UserRepo userRepo;
    private final AdminService adminService;

    @Value("${SUPERADMIN_SECRET}")
    private String superAdminSecret;

    public SuperAdminController(SuperAdminService superAdminService, UserMapper userMapper, AuthValidationService authValidationService, UserRepo userRepo, AdminService adminService) {
        this.superAdminService = superAdminService;
        this.userMapper = userMapper;
        this.authValidationService = authValidationService;
        this.userRepo = userRepo;
        this.adminService = adminService;
    }

    @PostMapping("/createSuperAdmin")
    public ResponseEntity<UserResponseDto> createSuperAdmin(
            @RequestHeader("X-SUPERADMIN-SECRET") String providedSecret,
            @RequestBody UserRequestDto dto) {

        if (!providedSecret.equals(superAdminSecret)) {
            throw new UserNotSuperAdmin("Invalid SuperAdmin Password");
        }

        User newUser = superAdminService.createSuperAdmin(dto);
        UserResponseDto responseDto = userMapper.toDto(newUser);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/createAdmin")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<UserResponseDto> createAdmin(@RequestBody UserRequestDto dto, Authentication auth, @RequestParam Long superAdminId) {

        authValidationService.validateSelfOrThrow(superAdminId, auth);

        User newUser = adminService.createAdmin(dto);
        UserResponseDto responseDto = userMapper.toDto(newUser);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/deleteAdmin/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id, Authentication auth) {
        authValidationService.validateSelfOrThrow(id, auth);

        userRepo.deleteById(id);
        return ResponseEntity.ok("Admin has been deleted");
    }

    @DeleteMapping("/deleteSuperAdmin/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<String> deleteSuperAdmin(@PathVariable Long id, Authentication auth, @RequestHeader("X-SUPERADMIN-SECRET") String providedSecret) {
        authValidationService.validateSelfOrThrow(id, auth);

        if (!providedSecret.equals(superAdminSecret)) {
            throw new UserNotSuperAdmin("Invalid SuperAdmin Password");
        }

        userRepo.deleteById(id);
        return ResponseEntity.ok("Super Admin has been deleted");
    }

    @GetMapping("/fetchAllUsers")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public List<User> fetchAllUsers(Authentication auth, @RequestParam Long superAdminId) {
        authValidationService.validateSelfOrThrow(superAdminId, auth);
        return userRepo.findAll();

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public Optional<User> fetchUser(@PathVariable Long id, Authentication auth, @RequestParam Long superAdminId) {
        authValidationService.validateSelfOrThrow(superAdminId, auth);

        return userRepo.findById(id);

    }

}
