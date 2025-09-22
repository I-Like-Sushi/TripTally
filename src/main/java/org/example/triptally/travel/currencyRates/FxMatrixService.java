package org.example.triptally.travel.currencyRates;


import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
public class FxMatrixService {

    private static final List<String> CURRENCIES = List.of(
            "USD","EUR","JPY","GBP","CNY","AUD","CAD","CHF","HKD","SGD","SEK","KRW"
    );

    private final FxRateRepository fxRateRepository;

    public FxMatrixService(FxRateRepository fxRateRepository) {
        this.fxRateRepository = fxRateRepository;
    }

    public void storeSnapshot(String base, Map<String, BigDecimal> baseRates, LocalDate asOfDate) {
        Map<String, BigDecimal> normalized = new HashMap<>(baseRates);
        normalized.put(base, BigDecimal.ONE);

        for (String from : CURRENCIES) {
            BigDecimal baseToFrom = normalized.get(from);
            for (String to : CURRENCIES) {
                BigDecimal rate = from.equals(to)
                        ? BigDecimal.ONE
                        : normalized.get(to).divide(baseToFrom, 10, RoundingMode.HALF_EVEN);

                fxRateRepository.save(new FxRate(from, to, rate, asOfDate));
            }
        }
    }
}
