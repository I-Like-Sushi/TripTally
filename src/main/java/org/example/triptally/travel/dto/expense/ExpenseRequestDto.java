package org.example.triptally.travel.dto.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import org.example.triptally.travel.currencyRates.AtLeastOneField;
import org.example.triptally.travel.model.enums.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

@AtLeastOneField(fields = { "amountLocal", "amountHome" })
public class ExpenseRequestDto {

    @NotBlank(message = "Description is required")
    private String description;

    @PositiveOrZero(message = "Amount in local currency must be positive or zero")
    private BigDecimal amountLocal;

    @PositiveOrZero(message = "Amount in local currency must be positive or zero")
    private BigDecimal amountHome;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "Category is required")
    private Category category;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmountLocal() { return amountLocal; }
    public void setAmountLocal(BigDecimal amountLocal) { this.amountLocal = amountLocal; }

    public BigDecimal getAmountHome() { return amountHome; }
    public void setAmountHome(BigDecimal amountHome) { this.amountHome = amountHome; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}

