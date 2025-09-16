package org.example.eindopdrachtbackend.travel.dto.trip;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TripUpdateDto {

    @NotBlank(message = "Destination is required")
    private String destination;

    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Positive(message = "Budget in home currency must be positive")
    private BigDecimal budgetHomeCurrency;

    @Positive(message = "Budget in local currency must be positive")
    private BigDecimal budgetLocalCurrency;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getBudgetHomeCurrency() {
        return budgetHomeCurrency;
    }

    public void setBudgetHomeCurrency(BigDecimal budgetHomeCurrency) {
        this.budgetHomeCurrency = budgetHomeCurrency;
    }

    public BigDecimal getBudgetLocalCurrency() {
        return budgetLocalCurrency;
    }

    public void setBudgetLocalCurrency(BigDecimal budgetLocalCurrency) {
        this.budgetLocalCurrency = budgetLocalCurrency;
    }
}
