package org.example.eindopdrachtbackend.travel.currencyRates;

import java.util.Currency;

public class CurrencyValidator {

    public static void validateCurrencyCode(String code, String fieldName) {
        try {
            if (!Currency.getAvailableCurrencies().contains(Currency.getInstance(code))) {
                throw new IllegalArgumentException("Invalid " + fieldName + ": " + code);
            }
        } catch (IllegalArgumentException e) {
            // Currency.getInstance throws IllegalArgumentException if code is null or invalid length
            throw new IllegalArgumentException("Invalid " + fieldName + ": " + code, e);
        }
    }
}
