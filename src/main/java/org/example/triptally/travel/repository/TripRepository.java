package org.example.triptally.travel.repository;

import org.example.triptally.travel.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, String> {

    Optional<Trip> findByTripId(String tripId);
    boolean existsByTripId(String tripId);
}
