package org.example.eindopdrachtbackend.auth;

import org.example.eindopdrachtbackend.auth.dto.LoginRequestDto;
import org.example.eindopdrachtbackend.auth.dto.LoginResponseDto;
import org.example.eindopdrachtbackend.exception.auth.InvalidLoginException;
import org.example.eindopdrachtbackend.security.JwtService;
import org.example.eindopdrachtbackend.user.UserMapper;
import org.example.eindopdrachtbackend.user.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
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
                            requestDto.getEmail(),
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
}
