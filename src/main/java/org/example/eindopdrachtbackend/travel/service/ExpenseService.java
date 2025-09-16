package org.example.eindopdrachtbackend.travel.service;

import org.example.eindopdrachtbackend.exception.trip.TripNotFoundException;
import org.example.eindopdrachtbackend.travel.dto.expense.ExpenseRequestDto;
import org.example.eindopdrachtbackend.travel.model.Expense;
import org.example.eindopdrachtbackend.travel.model.Trip;
import org.example.eindopdrachtbackend.travel.repository.ExpenseRepository;
import org.example.eindopdrachtbackend.travel.repository.TripRepository;
import org.example.eindopdrachtbackend.travel.tripMapper.ExpenseMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {


    private final TripRepository tripRepository;
    private final ExpenseMapper expenseMapper;
    private final ExpenseRepository expenseRepository;

    public ExpenseService(TripRepository tripRepository, ExpenseMapper expenseMapper, ExpenseRepository expenseRepository) {
        this.tripRepository = tripRepository;
        this.expenseMapper = expenseMapper;
        this.expenseRepository = expenseRepository;
    }

    public Expense createExpense(String tripId, ExpenseRequestDto dto) {

        Trip trip = tripRepository.findByTripId(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));

        Expense expense = expenseMapper.toEntity(dto);
        expense.setTrip(trip);
        expenseRepository.save(expense);

        return expense;
    }



}
