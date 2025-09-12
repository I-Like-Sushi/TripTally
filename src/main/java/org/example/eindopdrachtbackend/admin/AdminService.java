package org.example.eindopdrachtbackend.admin;

import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
import org.example.eindopdrachtbackend.user.User;
import org.example.eindopdrachtbackend.user.UserMapper;
import org.example.eindopdrachtbackend.user.UserRepo;
import org.example.eindopdrachtbackend.user.dto.UserRequestDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AdminService(UserRepo userRepo, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public User createAdmin(UserRequestDto dto) {
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        User user = userMapper.toEntity(dto);

        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        user.addRoles("ROLE_ADMIN");
        userRepo.save(user);
        return user;
    }

    public Long getCurrentUserId(Authentication auth) {
        String username = auth.getName();
        return userRepo.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

}
