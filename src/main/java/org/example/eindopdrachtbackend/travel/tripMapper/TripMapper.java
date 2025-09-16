package org.example.eindopdrachtbackend.travel.tripMapper;

import org.example.eindopdrachtbackend.travel.dto.trip.TripRequestDto;
import org.example.eindopdrachtbackend.travel.dto.trip.TripResponseDto;
import org.example.eindopdrachtbackend.travel.model.Trip;
import org.springframework.stereotype.Component;

@Component
public class TripMapper {

    public TripResponseDto toDto(Trip trip) {
        TripResponseDto dto = new TripResponseDto();
        dto.setTripId(trip.getTripId());
        dto.setDestination(trip.getDestination());
        dto.setDescription(trip.getDescription());
        dto.setStartDate(trip.getStartDate());
        dto.setEndDate(trip.getEndDate());
        dto.setBudgetHomeCurrency(trip.getBudgetHomeCurrency());
        dto.setBudgetLocalCurrency(trip.getBudgetLocalCurrency());
        return dto;
    }

    public Trip toEntity(TripRequestDto dto) {
        Trip trip = new Trip();
        trip.setDestination(dto.getDestination());
        trip.setDescription(dto.getDescription());
        trip.setStartDate(dto.getStartDate());
        trip.setEndDate(dto.getEndDate());
        trip.setBudgetHomeCurrency(dto.getBudgetHomeCurrency());
        trip.setBudgetLocalCurrency(dto.getBudgetLocalCurrency());
        return trip;
    }
}

