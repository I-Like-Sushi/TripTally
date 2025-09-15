package org.example.eindopdrachtbackend.travel.service;

import jakarta.transaction.Transactional;
import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
import org.example.eindopdrachtbackend.travel.TripMapper.TripMapper;
import org.example.eindopdrachtbackend.travel.dto.trip.TripRequestDto;
import org.example.eindopdrachtbackend.travel.model.Trip;
import org.example.eindopdrachtbackend.travel.repository.TripRepository;
import org.example.eindopdrachtbackend.user.User;
import org.example.eindopdrachtbackend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripMapper tripMapper;

    public TripService(TripRepository tripRepository, UserRepository userRepository, TripMapper tripMapper) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.tripMapper = tripMapper;
    }

    @Transactional
    public Trip createTrip(TripRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Trip trip = tripMapper.toEntity(dto);
        trip.setUser(user);

        String tripId = user.getId() + "_" +
                trip.getDestination().toUpperCase().replaceAll("\\s+", "_") + "_" +
                trip.getStartDate().toString().replaceAll("-", "") + "_" +
                UUID.randomUUID().toString().substring(0, 6);

        trip.setId(tripId);

        return tripRepository.save(trip);
    }


}


