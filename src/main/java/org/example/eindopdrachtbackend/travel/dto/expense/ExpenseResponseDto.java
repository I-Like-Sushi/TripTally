package org.example.eindopdrachtbackend.travel.dto.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.eindopdrachtbackend.travel.model.enums.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseResponseDto {

    private Long id;
    private String description;
    private BigDecimal amountLocal;
    private BigDecimal amountHome;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime timeStamp;
    private Category category;
    private String tripId;

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

    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }
}

