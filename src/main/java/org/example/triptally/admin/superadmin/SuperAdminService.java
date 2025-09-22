package org.example.triptally.admin.superadmin;

import org.example.triptally.user.User;
import org.example.triptally.user.UserMapper;
import org.example.triptally.user.UserRepository;
import org.example.triptally.user.UserService;
import org.example.triptally.user.dto.UserRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SuperAdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserService userService;

    public SuperAdminService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    public User createSuperAdmin(UserRequestDto dto) {
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        User user = userMapper.toEntity(dto);

        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        user.addRoles("ROLE_SUPERADMIN");
        user.setId(userService.generateUniqueId());
        userRepository.save(user);
        return user;

    }

}
