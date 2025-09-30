package org.example.triptally;

import org.example.triptally.travel.controller.ExpenseController;
import org.example.triptally.travel.dto.expense.ExpenseRequestDto;
import org.example.triptally.travel.dto.expense.ExpenseResponseDto;
import org.example.triptally.travel.mapper.ExpenseMapper;
import org.example.triptally.travel.model.Expense;
import org.example.triptally.travel.model.Trip;
import org.example.triptally.travel.model.enums.Category;
import org.example.triptally.travel.repository.TripRepository;
import org.example.triptally.travel.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;

import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseUnitTest {



    @Mock private ExpenseService expenseService;
    @Mock private ExpenseMapper expenseMapper;
    @Mock private Category category;
    @Mock private TripRepository tripRepository;
    @Mock private ExpenseResponseDto expenseResponseDto;
    @Mock private Expense expense;

    @InjectMocks
    private ExpenseController expenseController;

    @Test
    public void testCreateExpense() {

        // Arrange
        ExpenseRequestDto requestDto = new ExpenseRequestDto();
        requestDto.setDescription("Sushi");
        requestDto.setDate(LocalDate.of(2025, 9, 10));
        requestDto.setAmountHome(new BigDecimal("50.00"));
        requestDto.setCategory(Category.valueOf("FOOD"));

        Trip trip = tripRepository.findByTripId("TOKYO_20253010_5d873c").orElseThrow();

        when((Publisher<?>) expenseService.createExpense("TOKYO_20253010_5d873c", requestDto)).thenReturn(expense);

        // Act


        // Assert

    }

}
