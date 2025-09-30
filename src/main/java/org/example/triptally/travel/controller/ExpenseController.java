package org.example.triptally.travel.controller;

import jakarta.validation.Valid;
import org.example.triptally.auth.AuthValidationService;
import org.example.triptally.exception.trip.ExpenseNotFound;
import org.example.triptally.travel.dto.expense.ExpenseRequestDto;
import org.example.triptally.travel.dto.expense.ExpenseResponseDto;
import org.example.triptally.travel.dto.expense.ExpenseUpdateDto;
import org.example.triptally.travel.model.Expense;
import org.example.triptally.travel.repository.ExpenseRepository;
import org.example.triptally.travel.service.ExpenseService;
import org.example.triptally.travel.mapper.ExpenseMapper;
import org.example.triptally.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users/{userId}/trips/{tripId}/expenses")
@PreAuthorize("hasRole('USER')")
public class ExpenseController {

    private final AuthValidationService authValidationService;
    private final ExpenseService expenseService;
    private final ExpenseMapper expenseMapper;
    private final UserService userService;
    private final ExpenseRepository expenseRepository;

    public ExpenseController(AuthValidationService authValidationService, ExpenseService expenseService, ExpenseMapper expenseMapper, UserService userService, ExpenseRepository expenseRepository) {
        this.authValidationService = authValidationService;
        this.expenseService = expenseService;
        this.expenseMapper = expenseMapper;
        this.userService = userService;
        this.expenseRepository = expenseRepository;
    }
    
    @PostMapping
    public ResponseEntity<ExpenseResponseDto> createExpense(@PathVariable Long userId,
                                                            @PathVariable String tripId,
                                                            @Valid @RequestBody ExpenseRequestDto expenseRequestDto,
                                                            Authentication auth) {
        authValidationService.validateSelfOrThrow(userId, auth);

        Expense newExpense = expenseService.createExpense(tripId, expenseRequestDto);
        ExpenseResponseDto expenseResponseDto = expenseMapper.toDto(newExpense);

        URI location = URI.create("/api/users/" + userId + "/trips/" + tripId +  "/expenses/" + newExpense.getId());

        return ResponseEntity.created(location).body(expenseResponseDto);

    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponseDto> getExpense(@PathVariable Long userId,
                                                         @PathVariable String tripId,
                                                         @PathVariable Long expenseId,
                                                         Authentication auth) {
        authValidationService.validateSelfOrThrow(userId, auth);

        if (!userService.hasViewingAccess(auth, userId)) {
            throw new AccessDeniedException("You are not allowed to view this expense.");
        }

        Expense expense = expenseRepository.findByIdWithTrip(expenseId)
                .orElseThrow(() -> new ExpenseNotFound("Expense not Found"));

        if (!expense.getTrip().getTripId().equals(tripId)) {
            throw new AccessDeniedException("Expense does not belong to this trip.");
        }

        ExpenseResponseDto expenseResponseDto = expenseMapper.toDto(expense);

        return ResponseEntity.ok(expenseResponseDto);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long userId,
                                                @PathVariable String tripId,
                                                @PathVariable Long expenseId,
                                                Authentication auth) {
        authValidationService.validateSelfOrThrow(userId, auth);

        Expense expense = expenseRepository.findByIdWithTrip(expenseId).orElseThrow(() -> new ExpenseNotFound("Expense not Found"));

        if (!expense.getTrip().getTripId().equals(tripId)) {
            throw new AccessDeniedException("Expense does not belong to this trip.");
        }

        expenseRepository.delete(expense);

        return ResponseEntity.ok("Expense has been deleted.");
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable Long userId,
                                                            @PathVariable String tripId,
                                                            @PathVariable Long expenseId,
                                                            @Valid @RequestBody ExpenseUpdateDto expenseUpdateDto,
                                                            Authentication auth) {
        authValidationService.validateSelfOrThrow(userId, auth);

        Expense expense = expenseRepository.findByIdWithTrip(expenseId).orElseThrow(() -> new ExpenseNotFound("Expense not Found"));

        if (!expense.getTrip().getTripId().equals(tripId)) {
            throw new AccessDeniedException("Expense does not belong to this trip.");
        }

        expenseMapper.updateEntityFromDto(expenseUpdateDto, expense);

        expenseRepository.save(expense);

        return ResponseEntity.ok(expenseMapper.toDto(expense));

    }

}
