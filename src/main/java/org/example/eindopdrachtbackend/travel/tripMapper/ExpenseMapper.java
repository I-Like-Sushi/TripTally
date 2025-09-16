package org.example.eindopdrachtbackend.travel.tripMapper;

import org.example.eindopdrachtbackend.travel.dto.expense.ExpenseRequestDto;
import org.example.eindopdrachtbackend.travel.dto.expense.ExpenseResponseDto;
import org.example.eindopdrachtbackend.travel.dto.expense.ExpenseUpdateDto;
import org.example.eindopdrachtbackend.travel.model.Expense;
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
        dto.setTripId(expense.getTrip().getTripId());
        dto.setDate(expense.getDate());
        dto.setTimeStamp(expense.getTimeStamp());
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

    public void updateEntityFromDto(ExpenseUpdateDto dto, Expense expense){
        if (expense.getId()!=null){ expense.setId(expense.getId()); }
        if (expense.getCategory()!=null){ expense.setCategory(expense.getCategory()); }
        if (expense.getDescription()!=null){ expense.setDescription(expense.getDescription()); }
        if (expense.getAmountLocal()!=null){ expense.setAmountLocal(expense.getAmountLocal()); }
        if (expense.getAmountHome()!=null){ expense.setAmountHome(expense.getAmountHome()); }
    }
}
