package org.example.eindopdrachtbackend.admin.superadmin;

import org.example.eindopdrachtbackend.user.User;
import org.example.eindopdrachtbackend.user.UserMapper;
import org.example.eindopdrachtbackend.user.UserRepo;
import org.example.eindopdrachtbackend.user.dto.UserRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SuperAdminService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public SuperAdminService(UserRepo userRepo, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public User createSuperAdmin(UserRequestDto dto) {
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        User user = userMapper.toEntity(dto);

        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        user.addRoles("ROLE_SUPERADMIN");
        userRepo.save(user);
        return user;

    }

}
