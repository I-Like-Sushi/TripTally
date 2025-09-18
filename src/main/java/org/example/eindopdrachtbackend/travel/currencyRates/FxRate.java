package org.example.eindopdrachtbackend.travel.currencyRates;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@IdClass(FxRateId.class)
@Table(name = "fx_rate")
public class FxRate {

    @Id
    @Column(length = 3, nullable = false)
    private String baseCurrency;

    @Id
    @Column(length = 3, nullable = false)
    private String quoteCurrency;

    @Id
    @Column(nullable = false)
    private LocalDate asOfDate;

    @Column(precision = 34, scale = 18, nullable = false)
    private BigDecimal rate;

    public FxRate() {}

    public FxRate(String baseCurrency, String quoteCurrency, BigDecimal rate, LocalDate asOfDate) {
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.rate = rate;
        this.asOfDate = asOfDate;
    }

    public String getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }

    public String getQuoteCurrency() { return quoteCurrency; }
    public void setQuoteCurrency(String quoteCurrency) { this.quoteCurrency = quoteCurrency; }

    public LocalDate getAsOfDate() { return asOfDate; }
    public void setAsOfDate(LocalDate asOfDate) { this.asOfDate = asOfDate; }

    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }
}
