package org.example.eindopdrachtbackend.travel.repository;

import org.example.eindopdrachtbackend.travel.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, String> {
}
