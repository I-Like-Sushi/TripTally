package org.example.triptally.travel.mapper;

import org.example.triptally.travel.dto.expense.ExpenseRequestDto;
import org.example.triptally.travel.dto.expense.ExpenseResponseDto;
import org.example.triptally.travel.dto.expense.ExpenseUpdateDto;
import org.example.triptally.travel.model.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    public ExpenseResponseDto toDto(Expense expense){
        ExpenseResponseDto dto = new ExpenseResponseDto();
        dto.setId(expense.getId());
        dto.setDescription(expense.getDescription());
        dto.setCategory(expense.getCategory());
        dto.setAmountLocal(expense.getAmountLocal());
        dto.setAmountHome(expense.getAmountHome());
        dto.setDate(expense.getDate());
        dto.setTimeStamp(expense.getTimeStamp());
        dto.setTripId(expense.getTrip().getTripId());
        return dto;
    }

    public Expense toEntity(ExpenseRequestDto dto){
        Expense expense = new Expense();
        expense.setDescription(dto.getDescription());
        expense.setCategory(dto.getCategory());
        expense.setAmountLocal(dto.getAmountLocal());
        expense.setAmountHome(dto.getAmountHome());
        expense.setDate(dto.getDate());
        return expense;
    }

    public void updateEntityFromDto(ExpenseUpdateDto dto, Expense expense) {
        if (dto.getCategory() != null) expense.setCategory(dto.getCategory());
        if (dto.getDescription() != null) expense.setDescription(dto.getDescription());
    }
}
