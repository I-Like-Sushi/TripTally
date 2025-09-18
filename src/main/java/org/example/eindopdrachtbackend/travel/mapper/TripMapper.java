package org.example.eindopdrachtbackend.travel.mapper;

import org.example.eindopdrachtbackend.travel.dto.trip.TripRequestDto;
import org.example.eindopdrachtbackend.travel.dto.trip.TripResponseDto;
import org.example.eindopdrachtbackend.travel.dto.trip.TripUpdateDto;
import org.example.eindopdrachtbackend.travel.model.Trip;
import org.springframework.stereotype.Component;

@Component
public class TripMapper {

    private final ExpenseMapper expenseMapper;
    private final WishlistItemMapper wishlistItemMapper;

    public TripMapper(ExpenseMapper expenseMapper, WishlistItemMapper wishlistItemMapper) {
        this.expenseMapper = expenseMapper;
        this.wishlistItemMapper = wishlistItemMapper;
    }

    public TripResponseDto toDto(Trip trip) {
        TripResponseDto dto = new TripResponseDto();
        dto.setTripId(trip.getTripId());
        dto.setDestination(trip.getDestination());
        dto.setDescription(trip.getDescription());
        dto.setStartDate(trip.getStartDate());
        dto.setEndDate(trip.getEndDate());
        dto.setBudgetHomeCurrency(trip.getBudgetHomeCurrency());
        dto.setBudgetLocalCurrency(trip.getBudgetLocalCurrency());
        dto.setTripCreatedDate(trip.getTripCreatedDate());
        dto.setHomeCurrencyCode(trip.getHomeCurrencyCode());
        dto.setLocalCurrencyCode(trip.getLocalCurrencyCode());
        dto.setExpenses(trip.getExpenses().stream()
                .map(expenseMapper::toDto)
                .toList());
        dto.setWishlistItems(trip.getWishlistItems().stream()
                .map(wishlistItemMapper::toDto)
                .toList());
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
        trip.setHomeCurrencyCode(dto.getHomeCurrencyCode());
        trip.setLocalCurrencyCode(dto.getLocalCurrencyCode());
        return trip;
    }

    public void updateEntityFromDto(TripUpdateDto dto, Trip trip) {
        if (dto.getDestination() != null) trip.setDestination(dto.getDestination());
        if (dto.getDescription() != null) trip.setDescription(dto.getDescription());
        if (dto.getStartDate() != null) trip.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) trip.setEndDate(dto.getEndDate());
        if (dto.getBudgetHomeCurrency() != null) trip.setBudgetHomeCurrency(dto.getBudgetHomeCurrency());
        if (dto.getBudgetLocalCurrency() != null) trip.setBudgetLocalCurrency(dto.getBudgetLocalCurrency());
        if (dto.getHomeCurrencyCode() != null) trip.setHomeCurrencyCode(dto.getHomeCurrencyCode());
        if (dto.getLocalCurrencyCode() != null) trip.setLocalCurrencyCode(dto.getLocalCurrencyCode());
    }

}

