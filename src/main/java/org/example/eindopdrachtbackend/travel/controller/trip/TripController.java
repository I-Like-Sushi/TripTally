package org.example.eindopdrachtbackend.travel.controller.trip;

import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
import org.example.eindopdrachtbackend.travel.model.Trip;
import org.example.eindopdrachtbackend.travel.repository.TripRepository;
import org.example.eindopdrachtbackend.user.User;
import org.example.eindopdrachtbackend.user.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/trip")
public class TripController {

    private final UserRepository userRepo;
    private final TripRepository tripRepository;

    public TripController(UserRepository userRepo, TripRepository tripRepository) {
        this.userRepo = userRepo;
        this.tripRepository = tripRepository;
    }

    @PostMapping("/{userId}")
    public Trip createTrip(@RequestBody Trip trip, @PathVariable Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));


    }
}
