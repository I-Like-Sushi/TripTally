package org.example.eindopdrachtbackend.amin;

import org.example.eindopdrachtbackend.auth.AuthValidationService;
import org.example.eindopdrachtbackend.exception.auth.UserNotAdmin;
import org.example.eindopdrachtbackend.user.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepo userRepo;
    private final AuthValidationService authValidationService;

    public AdminController(UserRepo userRepo, AuthValidationService authValidationService) {
        this.userRepo = userRepo;
        this.authValidationService = authValidationService;
    }



    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id, @RequestParam long adminId, Authentication auth) {
        authValidationService.validateSelfOrThrow(adminId, auth);
        if (auth.getAuthorities().stream().anyMatch(a -> !a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new UserNotAdmin("You are not authorized for this action.");
        }



        String username = auth.getName();
        userRepo.deleteById(id);
        return ResponseEntity.ok("Account of " + username + " deleted successfully");

    }


}
