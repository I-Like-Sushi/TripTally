package org.example.eindopdrachtbackend.travel.model.currencyApiClient;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ExchangeRateService {

    private final CurrencyApiClient apiClient;
    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateService(CurrencyApiClient apiClient,
                               ExchangeRateRepository exchangeRateRepository) {
        this.apiClient = apiClient;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public void refreshRates(String baseCurrency) {
        CurrencyApiResponse response = apiClient.getRates(baseCurrency);

        response.getData().forEach((targetCurrency, rateData) -> {
            ExchangeRate rate = new ExchangeRate();
            rate.setBaseCurrency(baseCurrency);
            rate.setTargetCurrency(targetCurrency);
            rate.setRate(rateData.getValue()); // already BigDecimal in your DTO
            rate.setDate(LocalDate.now());

            exchangeRateRepository.save(rate);
        });
    }

    public BigDecimal convert(BigDecimal amount, String from, String to) {
        LocalDate today = LocalDate.now();
        ExchangeRate rate = exchangeRateRepository
                .findByBaseCurrencyAndTargetCurrencyAndDate(from, to, today)
                .orElseThrow(() -> new RuntimeException("Rate not found for today"));

        return amount.multiply(rate.getRate());
    }
}
