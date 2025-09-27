package org.example.triptally.travel.controller;

import jakarta.validation.Valid;
import org.example.triptally.auth.AuthValidationService;
import org.example.triptally.exception.trip.TripNotFoundException;
import org.example.triptally.travel.dto.trip.TripRequestDto;
import org.example.triptally.travel.dto.trip.TripResponseDto;
import org.example.triptally.travel.dto.trip.TripUpdateDto;
import org.example.triptally.travel.model.Trip;
import org.example.triptally.travel.repository.TripRepository;
import org.example.triptally.travel.service.TripService;
import org.example.triptally.travel.mapper.TripMapper;
import org.example.triptally.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/v1/users/{userId}/trips")
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

    @PostMapping
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

    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponseDto> getTrip(
            @PathVariable Long userId,
            @PathVariable String tripId,
            Authentication auth) {

        authValidationService.validateSelfOrThrow(userId, auth);

        if (!userService.hasViewingAccess(auth, userId)) {
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
        tripRepository.delete(trip);
        return ResponseEntity.ok("Trip to " + destination + " has been deleted.");
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<TripResponseDto> updateTrip(@PathVariable Long userId,
                                                      @RequestBody TripRequestDto dto,
                                                      Authentication auth,
                                                      @PathVariable String tripId) {
        authValidationService.validateSelfOrThrow(userId, auth);

        Trip updated = tripService.updateTrip(userId, tripId, dto);
        return ResponseEntity.ok(tripMapper.toDto(updated));
    }




}

