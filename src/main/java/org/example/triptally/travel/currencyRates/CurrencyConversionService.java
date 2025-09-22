package org.example.triptally.travel.currencyRates;

import org.example.triptally.travel.model.Trip;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;

@Service
public class CurrencyConversionService {

    private final FxRateRepository fxRateRepository;
    private final FxConfig fxConfig;

    public CurrencyConversionService(FxRateRepository fxRateRepository, FxConfig fxConfig) {
        this.fxRateRepository = fxRateRepository;
        this.fxConfig = fxConfig;
    }

    public BigDecimal convertToHomeCurrency(Trip trip, BigDecimal amountLocal) {
        return fxRateRepository.findByBaseCurrencyAndQuoteCurrencyAndAsOfDate(
                        trip.getLocalCurrencyCode(),
                        trip.getHomeCurrencyCode(),
                        fxConfig.getSnapshotDate()
                )
                .map(rate -> amountLocal.multiply(rate.getRate()))
                .orElseThrow(() -> new IllegalStateException(
                        "No FX rate found for " + trip.getLocalCurrencyCode() +
                                " → " + trip.getHomeCurrencyCode() +
                                " on " + fxConfig.getSnapshotDate()
                ));
    }

    public BigDecimal convertToLocalCurrency(Trip trip, BigDecimal amountHome) {
        return fxRateRepository.findByBaseCurrencyAndQuoteCurrencyAndAsOfDate(
                        trip.getLocalCurrencyCode(),
                        trip.getHomeCurrencyCode(),
                        fxConfig.getSnapshotDate()
                )
                .map(rate -> amountHome.divide(rate.getRate(), RoundingMode.HALF_UP))
                .orElseThrow(() -> new IllegalStateException(
                        "No FX rate found for " + trip.getLocalCurrencyCode() +
                                " → " + trip.getHomeCurrencyCode() +
                                " on " + fxConfig.getSnapshotDate()
                ));
    }

    /**
     * Fills in the missing amount (local or home) based on the one provided.
     * If both are provided, leaves them as-is.
     * If both are null, does nothing — should be caught by validation.
     */
    public void fillMissingAmounts(
            Trip trip,
            BigDecimal amountLocal,
            BigDecimal amountHome,
            Consumer<BigDecimal> setLocal,
            Consumer<BigDecimal> setHome
    ) {
        if (amountLocal != null && amountHome == null) {
            setLocal.accept(amountLocal);
            setHome.accept(convertToHomeCurrency(trip, amountLocal));
        } else if (amountHome != null && amountLocal == null) {
            setHome.accept(amountHome);
            setLocal.accept(convertToLocalCurrency(trip, amountHome));
        } else {
            setLocal.accept(amountLocal);
            setHome.accept(amountHome);
        }
    }
}
