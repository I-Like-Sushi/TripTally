package org.example.eindopdrachtbackend.travel.model.currencyApiClient;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateScheduler {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateScheduler(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    // Runs every day at 02:00
    @Scheduled(cron = "0 0 2 * * *")
    public void refreshAllSupportedCurrencies() {
        String[] baseCurrencies = {"JPY", "EUR", "USD"}; // add your supported bases
        for (String base : baseCurrencies) {
            exchangeRateService.refreshRates(base);
        }
    }
}

