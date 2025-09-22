package org.example.triptally.travel.dto.wishlist;

import org.example.triptally.travel.model.enums.Category;

import java.math.BigDecimal;

public class WishlistItemResponseDto {

    private String id;
    private String description;
    private BigDecimal amountLocal;
    private BigDecimal amountHome;
    private boolean purchased;
    private Category category;
    private String tripId;

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

    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }
}
