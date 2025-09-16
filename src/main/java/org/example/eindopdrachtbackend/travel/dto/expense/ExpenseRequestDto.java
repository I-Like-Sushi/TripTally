package org.example.eindopdrachtbackend.travel.dto.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import org.example.eindopdrachtbackend.travel.model.enums.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseRequestDto {

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount in local currency is required")
    @Positive(message = "Amount in local currency must be positive")
    private BigDecimal amountLocal;

    @NotNull(message = "Amount in home currency is required")
    @Positive(message = "Amount in home currency must be positive")
    private BigDecimal amountHome;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "Category is required")
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

