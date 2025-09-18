package org.example.eindopdrachtbackend.travel.service;

import org.example.eindopdrachtbackend.exception.trip.TripNotFoundException;
import org.example.eindopdrachtbackend.travel.currencyRates.CurrencyConversionService;
import org.example.eindopdrachtbackend.travel.dto.expense.ExpenseRequestDto;
import org.example.eindopdrachtbackend.travel.model.Expense;
import org.example.eindopdrachtbackend.travel.model.Trip;
import org.example.eindopdrachtbackend.travel.repository.ExpenseRepository;
import org.example.eindopdrachtbackend.travel.repository.TripRepository;
import org.example.eindopdrachtbackend.travel.mapper.ExpenseMapper;
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

