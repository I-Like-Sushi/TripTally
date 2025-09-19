package org.example.eindopdrachtbackend.travel.model;

import jakarta.persistence.*;
import org.example.eindopdrachtbackend.travel.model.enums.Category;
import java.math.BigDecimal;

@Entity
@Table(name = "wishlist_item")
public class WishlistItem {

    @Id
    @Column(updatable = false, nullable = false, unique = true)
    private String id;

    private String description;

    @Column(name = "amount_local")
    private BigDecimal amountLocal;
    @Column(name = "amount_home")
    private BigDecimal amountHome;

    private boolean purchased = false;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Enumerated(EnumType.STRING)
    private Category category;

    public WishlistItem() {}

    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmountLocal() { return amountLocal; }
    public void setAmountLocal(BigDecimal amountLocal) { this.amountLocal = amountLocal; }

    public BigDecimal getAmountHome() { return amountHome; }
    public void setAmountHome(BigDecimal amountHome) { this.amountHome = amountHome; }

    public boolean isPurchased() { return purchased; }
    public void setPurchased(boolean purchased) { this.purchased = purchased; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

}

