package org.example.eindopdrachtbackend.travel.dto.trip;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TripRequestDto {

    @NotBlank
    private String destination;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @FutureOrPresent
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Future
    private LocalDate endDate;

    @NotNull
    private BigDecimal budgetHomeCurrency;

    @NotNull
    private BigDecimal budgetLocalCurrency;

    @NotNull
    private Long userId;

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public BigDecimal getBudgetHomeCurrency() { return budgetHomeCurrency; }
    public void setBudgetHomeCurrency(BigDecimal budgetHomeCurrency) { this.budgetHomeCurrency = budgetHomeCurrency; }
    public BigDecimal getBudgetLocalCurrency() { return budgetLocalCurrency; }
    public void setBudgetLocalCurrency(BigDecimal budgetLocalCurrency) { this.budgetLocalCurrency = budgetLocalCurrency; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
