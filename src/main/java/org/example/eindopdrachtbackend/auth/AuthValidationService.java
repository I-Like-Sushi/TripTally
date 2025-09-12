package org.example.eindopdrachtbackend.auth;

import org.example.eindopdrachtbackend.user.User;
import org.example.eindopdrachtbackend.user.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthValidationService {

    private final UserRepo userRepo;

    public AuthValidationService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User validateSelfOrThrow(Long targetId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        User currentUser = userRepo.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));

        if (!currentUser.getId().equals(targetId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: you are not allowed to access this resource");
        }

        return currentUser;
    }
}