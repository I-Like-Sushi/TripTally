package org.example.triptally.travel.dto.wishlist;

import jakarta.validation.constraints.PositiveOrZero;
import org.example.triptally.travel.currencyRates.AtLeastOneField;
import org.example.triptally.travel.model.enums.Category;

import java.math.BigDecimal;

@AtLeastOneField(fields = { "amountLocal", "amountHome" })
public class WishlistItemUpdateDto {

    private String description;

    @PositiveOrZero(message = "Amount in local currency must be positive or zero")
    private BigDecimal amountLocal;

    @PositiveOrZero(message = "Amount in local currency must be positive or zero")
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
