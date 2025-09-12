package org.example.eindopdrachtbackend.auth;

import org.example.eindopdrachtbackend.auth.dto.LoginRequestDto;
import org.example.eindopdrachtbackend.auth.dto.LoginResponseDto;
import org.example.eindopdrachtbackend.exception.auth.InvalidLoginException;
import org.example.eindopdrachtbackend.exception.auth.UserNotAdmin;
import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
import org.example.eindopdrachtbackend.security.JwtService;
import org.example.eindopdrachtbackend.user.User;
import org.example.eindopdrachtbackend.user.UserMapper;
import org.example.eindopdrachtbackend.user.UserRepo;
import org.example.eindopdrachtbackend.user.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final UserRepo userRepo;
    private final AuthValidationService authValidationService;


    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager, UserMapper userMapper, UserRepo userRepo, AuthValidationService authValidationService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.userRepo = userRepo;
        this.authValidationService = authValidationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String username = authentication.getName();
            String token =  jwtService.generateToken(username);

            LoginResponseDto loginResponseDto = new LoginResponseDto("Login successful", token);

            return ResponseEntity.ok(loginResponseDto);
        } catch (AuthenticationException ex) {
            throw new InvalidLoginException("Username and/or password unknown");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication auth) {

        System.out.println(auth);

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String username = auth.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserResponseDto dto = userMapper.toDto(user);

        return ResponseEntity.ok(dto);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id, @RequestParam long adminId, Authentication auth) {
        authValidationService.validateSelfOrThrow(adminId, auth);
        if (auth.getAuthorities().stream().anyMatch(a -> !a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new UserNotAdmin("You are not authorized for this action.");
        }
        String username = auth.getName();
        userRepo.deleteById(id);
        return ResponseEntity.ok("Account of " + username + " deleted successfully");

    }


}
