package org.example.eindopdrachtbackend.travel.dto.wishlist;

import org.example.eindopdrachtbackend.travel.model.enums.Category;

import java.math.BigDecimal;

public class WishlistItemUpdateDto {

    private String description;
    private BigDecimal amountLocal;
    private BigDecimal amountHome;
    private Boolean purchased;
    private Category category;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmountLocal() { return amountLocal; }
    public void setAmountLocal(BigDecimal amountLocal) { this.amountLocal = amountLocal; }

    public BigDecimal getAmountHome() { return amountHome; }
    public void setAmountHome(BigDecimal amountHome) { this.amountHome = amountHome; }

    public Boolean getPurchased() { return purchased; }
    public void setPurchased(Boolean purchased) { this.purchased = purchased; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
