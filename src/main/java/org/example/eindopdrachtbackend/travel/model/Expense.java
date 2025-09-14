package org.example.eindopdrachtbackend.travel.model;

import jakarta.persistence.*;
import org.example.eindopdrachtbackend.travel.model.enums.Category;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    @Column(name = "amount_local")
    private BigDecimal amountLocal;
    @Column(name = "amount_home")
    private BigDecimal amountHome;

    private LocalDate date; // expense date

    @CreationTimestamp
    private LocalDateTime timeStamp; // auto-set when persisted

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Expense() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmountLocal() { return amountLocal; }
    public void setAmountLocal(BigDecimal amountLocal) { this.amountLocal = amountLocal; }

    public BigDecimal getAmountHome() { return amountHome; }
    public void setAmountHome(BigDecimal amountHome) { this.amountHome = amountHome; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalDateTime getTimeStamp() { return timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }
}
