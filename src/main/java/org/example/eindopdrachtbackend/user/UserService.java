package org.example.eindopdrachtbackend.user;

import org.example.eindopdrachtbackend.user.dto.UserRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserRequestDto dto) {
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        User user = userMapper.toEntity(dto);

        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        user.addRoles("USER");
        userRepo.save(user);
        return user;
    }

    public User createAdmin(UserRequestDto dto) {
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        User user = userMapper.toEntity(dto);

        user.setPassword(encryptedPassword);
        user.setEnabled(true);
        user.addRoles("ADMIN");
        userRepo.save(user);
        return user;
    }

}
