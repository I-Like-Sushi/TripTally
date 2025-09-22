package org.example.triptally.travel.service;

import org.example.triptally.exception.trip.TripNotFoundException;
import org.example.triptally.travel.currencyRates.CurrencyConversionService;
import org.example.triptally.travel.dto.expense.ExpenseRequestDto;
import org.example.triptally.travel.model.Expense;
import org.example.triptally.travel.model.Trip;
import org.example.triptally.travel.repository.ExpenseRepository;
import org.example.triptally.travel.repository.TripRepository;
import org.example.triptally.travel.mapper.ExpenseMapper;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    private final TripRepository tripRepository;
    private final ExpenseMapper expenseMapper;
    private final ExpenseRepository expenseRepository;
    private final CurrencyConversionService currencyConversionService;

    public ExpenseService(TripRepository tripRepository,
                          ExpenseMapper expenseMapper,
                          ExpenseRepository expenseRepository,
                          CurrencyConversionService currencyConversionService) {
        this.tripRepository = tripRepository;
        this.expenseMapper = expenseMapper;
        this.expenseRepository = expenseRepository;
        this.currencyConversionService = currencyConversionService;
    }

    public Expense createExpense(String tripId, ExpenseRequestDto dto) {
        Trip trip = tripRepository.findByTripId(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found"));

        Expense expense = expenseMapper.toEntity(dto);
        expense.setTrip(trip);

        if (dto.getDate() != null && dto.getDate().isBefore(trip.getStartDate())) {
            throw new IllegalArgumentException(
                    "Expense date cannot be earlier than the trip start date (" + trip.getStartDate() + ")"
            );
        }

        currencyConversionService.fillMissingAmounts(
                trip,
                dto.getAmountLocal(),
                dto.getAmountHome(),
                expense::setAmountLocal,
                expense::setAmountHome
        );

        return expenseRepository.save(expense);
    }

}

