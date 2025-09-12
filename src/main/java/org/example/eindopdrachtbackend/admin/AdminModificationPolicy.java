package org.example.eindopdrachtbackend.amin;

import org.example.eindopdrachtbackend.exception.auth.UserNotAdmin;
import org.example.eindopdrachtbackend.user.User;
import org.example.eindopdrachtbackend.user.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminModificationPolicy {

    private UserRepo userRepo;

    public AdminModificationPolicy(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void adminPolicy(Authentication auth, Long targetAdminId) throws UserNotAdmin {
        Optional<User> targetAdmin = userRepo.findById(targetAdminId);

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                && targetAdmin.stream().anyMatch(t -> t.getRoles().equals("ROLE_ADMIN"))
        ) {
            throw new UserNotAdmin("You are not authorized for this action.");
        }
    }

}
