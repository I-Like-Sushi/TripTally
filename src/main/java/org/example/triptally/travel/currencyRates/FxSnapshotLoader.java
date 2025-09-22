package org.example.triptally.travel.currencyRates;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

// ----------- USE ONCE ----------- //
@Component
public class FxSnapshotLoader implements CommandLineRunner {

    private final FxMatrixService fxMatrixService;

    public FxSnapshotLoader(FxMatrixService fxMatrixService) {
        this.fxMatrixService = fxMatrixService;
    }

    @Override
    public void run(String... args) {
        Map<String, BigDecimal> baseRates = Map.ofEntries(
                Map.entry("USD", new BigDecimal("1.1718")),
                Map.entry("JPY", new BigDecimal("173.33")),
                Map.entry("GBP", new BigDecimal("0.86530")),
                Map.entry("CNY", new BigDecimal("8.3461")),
                Map.entry("AUD", new BigDecimal("1.7646")),
                Map.entry("CAD", new BigDecimal("1.6227")),
                Map.entry("CHF", new BigDecimal("0.9347")),
                Map.entry("HKD", new BigDecimal("9.1198")),
                Map.entry("SGD", new BigDecimal("1.5042")),
                Map.entry("SEK", new BigDecimal("10.9485")),
                Map.entry("KRW", new BigDecimal("1630.98"))
        );
        fxMatrixService.storeSnapshot("EUR", baseRates, LocalDate.of(2025, 9, 17));
    }
}
