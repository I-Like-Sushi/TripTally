package org.example.triptally.user;

import jakarta.transaction.Transactional;
import org.example.triptally.exception.user.UserNotFoundException;
import org.example.triptally.user.dto.UserRequestDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random = new SecureRandom();

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Long generateUniqueId() {
        Long id;
        do {
            id = generateRandom12DigitNumber();
        } while (userRepository.existsById(id));
        return id;
    }

    private Long generateRandom12DigitNumber() {
        return 100_000_0000L + (Math.abs(random.nextLong()) % 900_000_0000L);
    }

    @Transactional
    public User createUser(UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(true);
        user.addRoles("ROLE_USER");
        if (user.getId() == null) {
            user.setId(generateUniqueId());
        }
        return userRepository.save(user);
    }

    public boolean hasViewingAccess(Authentication auth, Long targetId) {

        User loggedInUser = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        User targetUser = userRepository.findById(targetId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean hasViewingPermission = targetUser.getAllowedAccesView()
                .contains(loggedInUser.getUsername());

        return hasViewingPermission;
    }

    public boolean grantViewingAccess(Authentication auth, User targetUser) {
        User loggedInUser = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean alreadyAllowed = targetUser.getAllowedAccesView()
                .contains(loggedInUser.getUsername());

        if (!alreadyAllowed) {
            targetUser.addAllowedAccesView(loggedInUser.getUsername());
            loggedInUser.addAllowedAccesView(targetUser.getUsername());
            userRepository.save(targetUser);
            userRepository.save(loggedInUser);
            return true;
        }
        return false;
    }

    public boolean removeViewerAccess(Authentication auth, User targetUser) {
        User loggedInUser = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean currentlyAllowed = targetUser.getAllowedAccesView()
                .contains(loggedInUser.getUsername());

        if (currentlyAllowed) {
            targetUser.removeAllowedAccesView(loggedInUser.getUsername());
            loggedInUser.removeAllowedAccesView(targetUser.getUsername());
            userRepository.save(targetUser);
            userRepository.save(loggedInUser);
            return true;
        }
        return false;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
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
