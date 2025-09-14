package org.example.eindopdrachtbackend.travel.model;

import jakarta.persistence.*;
import org.example.eindopdrachtbackend.travel.model.enums.Category;

import java.math.BigDecimal;

@Entity
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(name = "estimated_cost_local")
    private BigDecimal estimatedCostLocal;
    @Column(name = "estimated_cost_home")
    private BigDecimal estimatedCostHome;

    private boolean purchased = false;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Enumerated(EnumType.STRING)
    private Category category;

    public WishlistItem() {}

    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getEstimatedCostLocal() { return estimatedCostLocal; }
    public void setEstimatedCostLocal(BigDecimal estimatedCostLocal) { this.estimatedCostLocal = estimatedCostLocal; }

    public BigDecimal getEstimatedCostHome() { return estimatedCostHome; }
    public void setEstimatedCostHome(BigDecimal estimatedCostHome) { this.estimatedCostHome = estimatedCostHome; }

    public boolean isPurchased() { return purchased; }
    public void setPurchased(boolean purchased) { this.purchased = purchased; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

}

