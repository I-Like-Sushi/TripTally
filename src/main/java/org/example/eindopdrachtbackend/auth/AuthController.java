package org.example.eindopdrachtbackend.auth;

import org.example.eindopdrachtbackend.auth.dto.LoginRequestDto;
import org.example.eindopdrachtbackend.auth.dto.LoginResponseDto;
import org.example.eindopdrachtbackend.exception.auth.InvalidLoginException;
import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
import org.example.eindopdrachtbackend.security.JwtService;
import org.example.eindopdrachtbackend.travel.mapper.TripMapper;
import org.example.eindopdrachtbackend.user.User;
import org.example.eindopdrachtbackend.user.UserMapper;
import org.example.eindopdrachtbackend.user.UserRepository;
import org.example.eindopdrachtbackend.user.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final TripMapper tripMapper;


    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager, UserMapper userMapper, UserRepository userRepository, TripMapper tripMapper) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.tripMapper = tripMapper;
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

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserResponseDto dto = userMapper.toDto(user);

        dto.setAllowViewingAccesTo(user.getAllowedAccesView());
        dto.setTrips(user.getTrips().stream()
                .map(tripMapper::toDto)
                .toList());
        // Admin not allowed to see who are friends with whom and what the active trips are. Only super admins are allowed that privilege.

        return ResponseEntity.ok(dto);

    }
}
