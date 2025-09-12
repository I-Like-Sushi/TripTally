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

    /**
     * Enforces the rule: an admin cannot modify another admin.
     *
     * @param auth           The currently authenticated user
     * @param targetAdminId  The ID of the user being modified
     * @throws UserNotAdmin if the current user is an admin and the target is also an admin
     */
    public void enforce(Authentication auth, Long targetAdminId) throws UserNotAdmin {
        Optional<User> targetUserOpt = userRepo.findById(targetAdminId);

        if (targetUserOpt.isEmpty()) {
            return; // or throw a NotFoundException if you want strict handling
        }

        User targetUser = targetUserOpt.get();

        boolean currentIsAdmin = hasRole(auth.getAuthorities(), "ROLE_ADMIN");
        boolean targetIsAdmin = targetUser.getRoles().stream()
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (currentIsAdmin && targetIsAdmin) {
            throw new UserNotAdmin("You are not authorized to modify another admin.");
        }
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }
}
