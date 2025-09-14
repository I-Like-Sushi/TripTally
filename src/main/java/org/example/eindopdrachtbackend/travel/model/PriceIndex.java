package org.example.eindopdrachtbackend.travel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class PriceIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destination;
    private String category;
    private BigDecimal averagePriceLocal;
    private BigDecimal averagePriceHome;
    private LocalDate lastUpdated;

    // Konbini locator
    // ect.


}

