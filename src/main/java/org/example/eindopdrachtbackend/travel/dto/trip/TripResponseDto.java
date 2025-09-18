package org.example.eindopdrachtbackend.travel.dto.trip;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import org.example.eindopdrachtbackend.travel.dto.expense.ExpenseResponseDto;
import org.example.eindopdrachtbackend.travel.dto.wishlist.WishlistItemResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tripCreatedDate;

    @Column(length = 3, nullable = false)
    private String homeCurrencyCode; // e.g. "EUR"

    @Column(length = 3, nullable = false)
    private String localCurrencyCode; // e.g. "JPY"

    @Positive(message = "Budget in home currency must be positive")
    private BigDecimal budgetHomeCurrency;

    @Positive(message = "Budget in local currency must be positive")
    private BigDecimal budgetLocalCurrency;

    private List<ExpenseResponseDto> expenses = new ArrayList<>();

    private List<WishlistItemResponseDto> wishlistItems = new ArrayList<>();

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

    public LocalDate getTripCreatedDate() { return tripCreatedDate; }
    public void setTripCreatedDate(LocalDate tripCreatedDate) { this.tripCreatedDate = tripCreatedDate; }

    public BigDecimal getBudgetHomeCurrency() { return budgetHomeCurrency; }
    public void setBudgetHomeCurrency(BigDecimal budgetHomeCurrency) { this.budgetHomeCurrency = budgetHomeCurrency; }

    public BigDecimal getBudgetLocalCurrency() { return budgetLocalCurrency; }
    public void setBudgetLocalCurrency(BigDecimal budgetLocalCurrency) { this.budgetLocalCurrency = budgetLocalCurrency; }

    public List<ExpenseResponseDto> getExpenses() { return expenses; }
    public void setExpenses(List<ExpenseResponseDto> expenses) { this.expenses = expenses != null ? new ArrayList<>(expenses) : new ArrayList<>(); }

    public List<WishlistItemResponseDto> getWishlistItems() { return wishlistItems; }

    public void setWishlistItems(List<WishlistItemResponseDto> wishlistItems) { this.wishlistItems = wishlistItems; }

    public String getHomeCurrencyCode() { return homeCurrencyCode; }
    public void setHomeCurrencyCode(String homeCurrencyCode) { this.homeCurrencyCode = homeCurrencyCode; }

    public String getLocalCurrencyCode() { return localCurrencyCode; }
    public void setLocalCurrencyCode(String localCurrencyCode) { this.localCurrencyCode = localCurrencyCode; }
}
