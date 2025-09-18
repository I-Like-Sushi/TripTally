package org.example.eindopdrachtbackend.travel.model;

import jakarta.persistence.*;
import org.example.eindopdrachtbackend.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @Column(updatable = false, nullable = false)
    private String tripId;

    @Column(nullable = false)
    private String destination;

    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal budgetHomeCurrency;
    private BigDecimal budgetLocalCurrency;

    @Column(length = 3, nullable = false)
    private String homeCurrencyCode; // e.g. "EUR"

    @Column(length = 3, nullable = false)
    private String localCurrencyCode; // e.g. "JPY"

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate tripCreatedDate;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> wishlistItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Trip() {}

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

    public List<Expense> getExpenses() { return expenses; }
    public void setExpenses(List<Expense> expenses) {
        this.expenses.clear();
        if (expenses != null) {
            expenses.forEach(this::addExpense);
        }
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        expense.setTrip(this);
    }

    public void removeExpense(Expense expense) {
        expenses.remove(expense);
        expense.setTrip(null);
    }

    public List<WishlistItem> getWishlistItems() { return wishlistItems; }
    public void setWishlistItems(List<WishlistItem> wishlistItems) {
        this.wishlistItems.clear();
        if (wishlistItems != null) {
            wishlistItems.forEach(this::addWishlistItem);
        }
    }

    public void addWishlistItem(WishlistItem item) {
        wishlistItems.add(item);
        item.setTrip(this);
    }

    public void removeWishlistItem(WishlistItem item) {
        wishlistItems.remove(item);
        item.setTrip(null);
    }

    public LocalDate getTripCreatedDate() { return tripCreatedDate; }
    public void setTripCreatedDate(LocalDate tripCreatedDate) { this.tripCreatedDate = tripCreatedDate; }

    public String getHomeCurrencyCode() { return homeCurrencyCode; }
    public void setHomeCurrencyCode(String homeCurrencyCode) { this.homeCurrencyCode = homeCurrencyCode; }

    public String getLocalCurrencyCode() { return localCurrencyCode; }
    public void setLocalCurrencyCode(String localCurrencyCode) { this.localCurrencyCode = localCurrencyCode; }

}
