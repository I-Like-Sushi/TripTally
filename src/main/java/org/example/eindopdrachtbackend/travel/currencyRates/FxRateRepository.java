package org.example.eindopdrachtbackend.travel.currencyRates;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface FxRateRepository extends JpaRepository<FxRate, FxRateId> {
    Optional<FxRate> findByBaseCurrencyAndQuoteCurrencyAndAsOfDate(
            String baseCurrency, String quoteCurrency, LocalDate asOfDate
    );
}
