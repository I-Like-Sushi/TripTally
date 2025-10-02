package org.example.triptally.unit;

import org.example.triptally.exception.trip.TripNotFoundException;
import org.example.triptally.travel.currencyRates.CurrencyConversionService;
import org.example.triptally.travel.dto.expense.ExpenseRequestDto;
import org.example.triptally.travel.model.Expense;
import org.example.triptally.travel.model.Trip;
import org.example.triptally.travel.model.enums.Category;
import org.example.triptally.travel.repository.ExpenseRepository;
import org.example.triptally.travel.repository.TripRepository;
import org.example.triptally.travel.mapper.ExpenseMapper;
import org.example.triptally.travel.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock TripRepository tripRepository;
    @Mock ExpenseMapper expenseMapper;
    @Mock ExpenseRepository expenseRepository;
    @Mock CurrencyConversionService currencyConversionService;

    @InjectMocks
    ExpenseService expenseService;

    @Test
    void createExpense_success_calls_conversion_and_saves() {
        // Arrange
        String tripId = "TOKYO_20253010_5d873c";
        Trip trip = new Trip();
        trip.setTripId(tripId);
        trip.setStartDate(LocalDate.of(2025, 10, 10));

        ExpenseRequestDto dto = new ExpenseRequestDto();
        dto.setDescription("Lunch");
        dto.setCategory(Category.FOOD);
        dto.setAmountLocal(new BigDecimal("1500")); // local amount
        dto.setAmountHome(null);
        dto.setDate(LocalDate.of(2025, 10, 12));

        Expense mapped = new Expense();
        mapped.setDescription(dto.getDescription());
        mapped.setCategory(dto.getCategory());
        mapped.setAmountLocal(dto.getAmountLocal());
        mapped.setAmountHome(dto.getAmountHome());
        mapped.setDate(dto.getDate());

        when(tripRepository.findByTripId(tripId)).thenReturn(Optional.of(trip));
        when(expenseMapper.toEntity(dto)).thenReturn(mapped);

        // Simulate repository save returning the entity with id set
        Expense saved = new Expense();
        saved.setId(42L);
        saved.setDescription(mapped.getDescription());
        saved.setCategory(mapped.getCategory());
        saved.setAmountLocal(mapped.getAmountLocal());
        saved.setAmountHome(new BigDecimal("12.34"));
        saved.setDate(mapped.getDate());
        saved.setTrip(trip);

        when(expenseRepository.save(any(Expense.class))).thenReturn(saved);

        // Act
        Expense result = expenseService.createExpense(tripId, dto);

        // Assert - conversion service was called to fill missing amounts, with mapped trip & amounts
        verify(currencyConversionService).fillMissingAmounts(
                eq(trip),
                eq(dto.getAmountLocal()),
                eq(dto.getAmountHome()),
                Mockito.<Consumer<BigDecimal>>any(),
                Mockito.<Consumer<BigDecimal>>any()
        );

        ArgumentCaptor<Expense> captor = ArgumentCaptor.forClass(Expense.class);
        verify(expenseRepository).save(captor.capture());

        Expense passedToSave = captor.getValue();
        assertThat(passedToSave.getTrip()).isSameAs(trip);
        assertThat(passedToSave.getDescription()).isEqualTo("Lunch");
        assertThat(result).isSameAs(saved);
        assertThat(result.getId()).isEqualTo(42L);
    }

    @Test
    void createExpense_missingTrip_throwsTripNotFoundException() {
        // Arrange
        String tripId = "NON_EXISTENT";
        ExpenseRequestDto dto = new ExpenseRequestDto();

        when(tripRepository.findByTripId(tripId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> expenseService.createExpense(tripId, dto))
                .isInstanceOf(TripNotFoundException.class)
                .hasMessageContaining("Trip not found");

        verifyNoInteractions(expenseMapper, expenseRepository, currencyConversionService);
    }

    @Test
    void createExpense_dateBeforeTripStart_throwsIllegalArgumentException() {
        // Arrange
        String tripId = "TOKYO_20253010_5d873c";
        Trip trip = new Trip();
        trip.setTripId(tripId);
        trip.setStartDate(LocalDate.of(2025, 10, 10));

        ExpenseRequestDto dto = new ExpenseRequestDto();
        dto.setDate(LocalDate.of(2025, 10, 9));

        when(tripRepository.findByTripId(tripId)).thenReturn(Optional.of(trip));
        when(expenseMapper.toEntity(dto)).thenReturn(new Expense());

        // Act & Assert
        assertThatThrownBy(() -> expenseService.createExpense(tripId, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expense date cannot be earlier than the trip start date");

        verify(tripRepository).findByTripId(tripId);
        verifyNoInteractions(currencyConversionService, expenseRepository);
    }
}
