package org.example.eindopdrachtbackend.travel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal budgetHomeCurrency;
    private BigDecimal budgetLocalCurrency;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> wishlistItems = new ArrayList<>();

    public Trip() {}

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public List<Expense> getExpenses() { return expenses; }
    public void setExpenses(List<Expense> expenses) {
        this.expenses.clear();
        if (expenses != null) {
            expenses.forEach(this::addExpense);
        }
    }

    public List<WishlistItem> getWishlistItems() { return wishlistItems; }
    public void setWishlistItems(List<WishlistItem> wishlistItems) {
        this.wishlistItems.clear();
        if (wishlistItems != null) {
            wishlistItems.forEach(this::addWishlistItem);
        }
    }

    // --- Relationship helpers ---
    public void addExpense(Expense expense) {
        expenses.add(expense);
        expense.setTrip(this); // keep owning side in sync
    }

    public void removeExpense(Expense expense) {
        expenses.remove(expense);
        expense.setTrip(null); // break the link
    }

    public void addWishlistItem(WishlistItem item) {
        wishlistItems.add(item);
        item.setTrip(this); // keep owning side in sync
    }

    public void removeWishlistItem(WishlistItem item) {
        wishlistItems.remove(item);
        item.setTrip(null); // break the link
    }
}
