package org.example.eindopdrachtbackend.user;

import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
import org.example.eindopdrachtbackend.user.dto.UserRequestDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {

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
        user.addRoles("ROLE_USER");
        userRepo.save(user);
        return user;
    }

    public boolean grantViewingAccess(Authentication auth, User targetUser) {
        User loggedInUser = userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean alreadyAllowed = targetUser.getAllowedAccesView()
                .contains(loggedInUser.getUsername());

        if (!alreadyAllowed) {
            targetUser.addAllowedAccesView(loggedInUser.getUsername());
            loggedInUser.addAllowedAccesView(targetUser.getUsername());
            userRepo.save(targetUser);
            userRepo.save(loggedInUser);
            return true;
        }
        return false;
    }

    public boolean removeViewerAccess(Authentication auth, User targetUser) {
        User loggedInUser = userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean currentlyAllowed = targetUser.getAllowedAccesView()
                .contains(loggedInUser.getUsername());

        if (currentlyAllowed) {
            targetUser.removeAllowedAccesView(loggedInUser.getUsername());
            loggedInUser.removeAllowedAccesView(targetUser.getUsername());
            userRepo.save(targetUser);
            userRepo.save(loggedInUser);
            return true;
        }
        return false;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountLocked(!user.isEnabled())
                .disabled(!user.isEnabled())
                .build();
    }


}
