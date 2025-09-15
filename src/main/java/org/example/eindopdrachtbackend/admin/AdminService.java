package org.example.eindopdrachtbackend.admin;

import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
import org.example.eindopdrachtbackend.user.User;
import org.example.eindopdrachtbackend.user.UserMapper;
import org.example.eindopdrachtbackend.user.UserRepository;
import org.example.eindopdrachtbackend.user.UserService;
import org.example.eindopdrachtbackend.user.dto.UserRequestDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserService userService;

    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    public User createAdmin(UserRequestDto dto) {
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        User user = userMapper.toEntity(dto);

        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        user.addRoles("ROLE_ADMIN");
        user.setId(userService.generateUniqueId());
        userRepository.save(user);
        return user;
    }

    public Long getCurrentUserId(Authentication auth) {
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

}
