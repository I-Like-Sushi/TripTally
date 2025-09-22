package org.example.triptally.travel.dto.trip;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.example.triptally.travel.currencyRates.AtLeastOneField;

import java.math.BigDecimal;
import java.time.LocalDate;

@AtLeastOneField(fields = { "budgetLocalCurrency", "budgetHomeCurrency" })
public class TripUpdateDto {

    @NotBlank(message = "Destination is required")
    private String destination;

    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @PositiveOrZero(message = "Amount in local currency must be positive or zero")
    private BigDecimal budgetHomeCurrency;

    @PositiveOrZero(message = "Amount in local currency must be positive or zero")
    private BigDecimal budgetLocalCurrency;

    @Column(length = 3, nullable = false)
    private String homeCurrencyCode; // e.g. "EUR"

    @Column(length = 3, nullable = false)
    private String localCurrencyCode; // e.g. "JPY"

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

    public String getHomeCurrencyCode() { return homeCurrencyCode; }
    public void setHomeCurrencyCode(String homeCurrencyCode) { this.homeCurrencyCode = homeCurrencyCode; }

    public String getLocalCurrencyCode() { return localCurrencyCode; }
    public void setLocalCurrencyCode(String localCurrencyCode) { this.localCurrencyCode = localCurrencyCode; }
}
