package org.example.eindopdrachtbackend.travel.controller;

import jakarta.validation.Valid;
import org.example.eindopdrachtbackend.auth.AuthValidationService;
import org.example.eindopdrachtbackend.travel.dto.expense.ExpenseRequestDto;
import org.example.eindopdrachtbackend.travel.dto.expense.ExpenseResponseDto;
import org.example.eindopdrachtbackend.travel.model.Expense;
import org.example.eindopdrachtbackend.travel.service.ExpenseService;
import org.example.eindopdrachtbackend.travel.tripMapper.ExpenseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users/{userId}/trips/{tripId}/expenses")
@PreAuthorize("hasRole('USER')")
public class ExpenseController {

    private final AuthValidationService authValidationService;
    private final ExpenseService expenseService;
    private final ExpenseMapper expenseMapper;

    public ExpenseController(AuthValidationService authValidationService, ExpenseService expenseService, ExpenseMapper expenseMapper) {
        this.authValidationService = authValidationService;
        this.expenseService = expenseService;
        this.expenseMapper = expenseMapper;
    }
    
    @PostMapping("/create-expense")
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
}
