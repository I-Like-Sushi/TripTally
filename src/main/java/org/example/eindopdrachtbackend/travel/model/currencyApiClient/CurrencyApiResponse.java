package org.example.eindopdrachtbackend.travel.model.currencyApiClient;

import java.math.BigDecimal;
import java.util.Map;

public class CurrencyApiResponse {
    private Map<String, CurrencyData> data;

    public Map<String, CurrencyData> getData() { return data; }

    public void setData(Map<String, CurrencyData> data) { this.data = data; }

    public static class CurrencyData {
        private String code;
        private BigDecimal value;

        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }

        public BigDecimal getValue() {
            return value;
        }
        public void setValue(BigDecimal value) {
            this.value = value;
        }
    }
}

