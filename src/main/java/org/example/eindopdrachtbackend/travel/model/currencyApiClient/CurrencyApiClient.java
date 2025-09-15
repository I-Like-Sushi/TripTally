package org.example.eindopdrachtbackend.travel.model.currencyApiClient;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CurrencyApiClient {

    private final WebClient webClient;

    public CurrencyApiClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://api.currencyapi.com/v3")
                .build();
    }

    public CurrencyApiResponse getRates(String baseCurrency) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("apikey","THE_API_KEY")
                        .queryParam("base_currency", baseCurrency)
                        .build())
                .retrieve()
                .bodyToMono(CurrencyApiResponse.class)
                .block();
    }
}
