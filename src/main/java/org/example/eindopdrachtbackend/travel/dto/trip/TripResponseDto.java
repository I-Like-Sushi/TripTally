package org.example.eindopdrachtbackend.travel.dto.trip;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TripResponseDto {

    private String tripId;

    private String destination;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Future
    private LocalDate endDate;
    private BigDecimal budgetHomeCurrency;
    private BigDecimal budgetLocalCurrency;

    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BigDecimal getBudgetHomeCurrency() { return budgetHomeCurrency; }
    public void setBudgetHomeCurrency(BigDecimal budgetHomeCurrency) { this.budgetHomeCurrency = budgetHomeCurrency; }

    public BigDecimal getBudgetLocalCurrency() { return budgetLocalCurrency; }
    public void setBudgetLocalCurrency(BigDecimal budgetLocalCurrency) { this.budgetLocalCurrency = budgetLocalCurrency; }
}

