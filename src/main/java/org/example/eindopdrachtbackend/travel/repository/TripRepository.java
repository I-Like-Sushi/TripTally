package org.example.eindopdrachtbackend.travel.repository;

import org.example.eindopdrachtbackend.travel.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, String> {

    Optional<Trip> findByTripId(String tripId);
    boolean existsByTripId(String tripId);
}
