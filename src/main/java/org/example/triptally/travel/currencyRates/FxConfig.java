package org.example.triptally.travel.currencyRates;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class FxConfig {

    private final LocalDate snapshotDate;

    public FxConfig(@Value("${fx.snapshot-date}") LocalDate snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    public LocalDate getSnapshotDate() {
        return snapshotDate;
    }
}
