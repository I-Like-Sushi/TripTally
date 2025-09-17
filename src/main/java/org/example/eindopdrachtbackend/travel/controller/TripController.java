package org.example.eindopdrachtbackend.travel.controller;

import jakarta.validation.Valid;
import org.example.eindopdrachtbackend.auth.AuthValidationService;
import org.example.eindopdrachtbackend.exception.trip.TripNotFoundException;
import org.example.eindopdrachtbackend.travel.dto.trip.TripRequestDto;
import org.example.eindopdrachtbackend.travel.dto.trip.TripResponseDto;
import org.example.eindopdrachtbackend.travel.dto.trip.TripUpdateDto;
import org.example.eindopdrachtbackend.travel.model.Trip;
import org.example.eindopdrachtbackend.travel.repository.TripRepository;
import org.example.eindopdrachtbackend.travel.service.TripService;
import org.example.eindopdrachtbackend.travel.mapper.TripMapper;
import org.example.eindopdrachtbackend.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/users/{userId}/trips")
public class TripController {

    private final AuthValidationService authValidationService;
    private final TripMapper tripMapper;
    private final TripService tripService;
    private final TripRepository tripRepository;
    private final UserService userService;

    public TripController(AuthValidationService authValidationService, TripMapper tripMapper, TripService tripService, TripRepository tripRepository, UserService userService) {
        this.authValidationService = authValidationService;
        this.tripMapper = tripMapper;
        this.tripService = tripService;
        this.tripRepository = tripRepository;
        this.userService = userService;
    }

    @PostMapping("/create-trip")
    public ResponseEntity<TripResponseDto> createTrip(
            @PathVariable Long userId,
            @Valid @RequestBody TripRequestDto dto,
            Authentication auth) {

        authValidationService.validateSelfOrThrow(userId, auth);

        Trip newTrip = tripService.createTrip(userId, dto);
        TripResponseDto responseDto = tripMapper.toDto(newTrip);

        URI location = URI.create("/api/users/" + userId + "/trips/" + newTrip.getTripId());
        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/{targetId}/{tripId}")  // the logged-in user can get his own data from "/auth/me"
    public ResponseEntity<TripResponseDto> getTrip(
            @PathVariable Long userId,
            @PathVariable Long targetId,
            @PathVariable String tripId,
            Authentication auth) {

        authValidationService.validateSelfOrThrow(userId, auth);

        if (!userService.hasViewingAccess(auth, targetId)) {
            throw new AccessDeniedException("You are not allowed to view this trip.");
        }

        Trip trip = tripRepository.findByTripId(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found."));
        return ResponseEntity.ok(tripMapper.toDto(trip));
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<String> deleteTrip(@PathVariable Long userId, @PathVariable String tripId, Authentication auth) {
        authValidationService.validateSelfOrThrow(userId, auth);
        Trip trip = tripRepository.findByTripId(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found."));
        String destination = trip.getDestination();
        return ResponseEntity.ok("Trip to " + destination + " has been deleted.");
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<TripResponseDto> updateTrip(@PathVariable Long userId,
                                                      @Valid @RequestBody TripUpdateDto dto,
                                                      Authentication auth,
                                                      @PathVariable String tripId) {
        authValidationService.validateSelfOrThrow(userId, auth);

        Trip trip = tripRepository.findByTripId(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found."));

        tripMapper.updateEntityFromDto(dto, trip);
        tripRepository.save(trip);
        return ResponseEntity.ok(tripMapper.toDto(trip));
    }



}

