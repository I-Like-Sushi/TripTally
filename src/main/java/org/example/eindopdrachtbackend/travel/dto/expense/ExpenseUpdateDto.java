package org.example.eindopdrachtbackend.travel.dto.expense;


import jakarta.validation.constraints.PositiveOrZero;
import org.example.eindopdrachtbackend.travel.currencyRates.AtLeastOneField;
import org.example.eindopdrachtbackend.travel.model.enums.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

@AtLeastOneField(fields = { "amountLocal", "amountHome" })
public class ExpenseUpdateDto {

    private String description;

    @PositiveOrZero(message = "Amount in local currency must be positive or zero")
    private BigDecimal amountLocal;

    @PositiveOrZero(message = "Amount in local currency must be positive or zero")
    private BigDecimal amountHome;

    private LocalDate date;

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

