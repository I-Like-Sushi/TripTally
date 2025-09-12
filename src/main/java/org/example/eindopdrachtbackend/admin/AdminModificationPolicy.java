package org.example.eindopdrachtbackend.admin;

import org.example.eindopdrachtbackend.exception.auth.UserNotAdmin;
import org.example.eindopdrachtbackend.user.User;
import org.example.eindopdrachtbackend.user.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class AdminModificationPolicy {

    private final UserRepo userRepo;

    public AdminModificationPolicy(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void enforce(Authentication auth, Long targetUserId) throws UserNotAdmin {
        Optional<User> targetUserOpt = userRepo.findById(targetUserId);
        if (targetUserOpt.isEmpty()) {
            return; // or throw NotFoundException
        }

        User targetUser = targetUserOpt.get();

        boolean currentIsAdmin = hasRole(auth.getAuthorities(), "ROLE_ADMIN");
        boolean currentIsSuperAdmin = hasRole(auth.getAuthorities(), "ROLE_SUPERADMIN");

        boolean targetIsAdminOrSuper = targetUser.getRoles().stream()
                .anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_SUPERADMIN"));

        // Only block if the current user is an admin (not superadmin) and the target is admin/superadmin
        if (currentIsAdmin && !currentIsSuperAdmin && targetIsAdminOrSuper) {
            throw new UserNotAdmin("You are not authorized to modify another admin or superadmin.");
        }
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        return authorities.stream().anyMatch(a -> a.getAuthority().equals(role));
    }

}
