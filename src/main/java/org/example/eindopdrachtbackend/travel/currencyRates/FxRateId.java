package org.example.eindopdrachtbackend.travel.currencyRates;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class FxRateId implements Serializable {
    private String baseCurrency;
    private String quoteCurrency;
    private LocalDate asOfDate;

    public FxRateId() {}

    public FxRateId(String baseCurrency, String quoteCurrency, LocalDate asOfDate) {
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.asOfDate = asOfDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FxRateId)) return false;
        FxRateId that = (FxRateId) o;
        return Objects.equals(baseCurrency, that.baseCurrency) &&
                Objects.equals(quoteCurrency, that.quoteCurrency) &&
                Objects.equals(asOfDate, that.asOfDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrency, quoteCurrency, asOfDate);
    }
}
