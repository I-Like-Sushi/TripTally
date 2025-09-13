package org.example.eindopdrachtbackend.admin;

import org.example.eindopdrachtbackend.exception.auth.ForbiddenAction;
import org.example.eindopdrachtbackend.exception.user.UserNotAdmin;
import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
import org.example.eindopdrachtbackend.user.User;
import org.example.eindopdrachtbackend.user.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AdminModificationPolicy {

    private final UserRepo userRepo;

    public AdminModificationPolicy(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void enforce(Authentication auth, Long targetUserId) {
        User targetUser = userRepo.findById(targetUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Long currentUserId = userRepo.findByUsername(auth.getName())
                .map(User::getId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean userIsMyself = currentUserId.equals(targetUserId);

        boolean currentIsAdmin      = hasAuthority(auth.getAuthorities(), "ROLE_ADMIN");
        boolean currentIsSuperAdmin = hasAuthority(auth.getAuthorities(), "ROLE_SUPERADMIN");

        boolean targetIsAdmin      = targetUser.getRoles().contains("ROLE_ADMIN");
        boolean targetIsSuperAdmin = targetUser.getRoles().contains("ROLE_SUPERADMIN");

        if (currentIsAdmin && !currentIsSuperAdmin && (targetIsAdmin || targetIsSuperAdmin)) {
            throw new UserNotAdmin("You are not authorized to modify another admin or superadmin.");
        }

        if (currentIsSuperAdmin && targetIsSuperAdmin && !userIsMyself) {
            throw new ForbiddenAction("You are not authorized to modify another superadmin.");
        }
    }

    private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String roleName) {
        return authorities.stream().anyMatch(a -> a.getAuthority().equals(roleName));
    }



}
