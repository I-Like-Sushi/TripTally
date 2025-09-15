package org.example.eindopdrachtbackend.travel.model.currencyApiClient;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "exchange_rates")
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 3)
    private String baseCurrency; // e.g., "JPY"

    @Column(nullable = false, length = 3)
    private String targetCurrency; // e.g., "USD"

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal rate; // e.g., 0.006300

    @Column(nullable = false)
    private LocalDate date; // date the rate applies to

    // --- Constructors ---
    public ExchangeRate() {}

    public ExchangeRate(String baseCurrency, String targetCurrency, BigDecimal rate, LocalDate date) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.date = date;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public String getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }
    public String getTargetCurrency() { return targetCurrency; }
    public void setTargetCurrency(String targetCurrency) { this.targetCurrency = targetCurrency; }
    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}

