package org.example.eindopdrachtbackend.travel.mapper;

import org.example.eindopdrachtbackend.travel.dto.wishlist.WishlistItemRequestDto;
import org.example.eindopdrachtbackend.travel.dto.wishlist.WishlistItemResponseDto;
import org.example.eindopdrachtbackend.travel.dto.wishlist.WishlistItemUpdateDto;
import org.example.eindopdrachtbackend.travel.model.Trip;
import org.example.eindopdrachtbackend.travel.model.WishlistItem;
import org.springframework.stereotype.Component;

@Component
public class WishlistItemMapper {

    public WishlistItemResponseDto toDto(WishlistItem wishlistItem) {
        WishlistItemResponseDto dto = new WishlistItemResponseDto();
        dto.setId(wishlistItem.getId());
        dto.setDescription(wishlistItem.getDescription());
        dto.setAmountLocal(wishlistItem.getAmountLocal());
        dto.setAmountHome(wishlistItem.getAmountHome());
        dto.setPurchased(wishlistItem.isPurchased());
        dto.setCategory(wishlistItem.getCategory());

        if (wishlistItem.getTrip() != null) {
            dto.setTripId(wishlistItem.getTrip().getTripId());
        }

        return dto;
    }

    public WishlistItem toEntity(WishlistItemRequestDto dto, Trip trip) {
        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setDescription(dto.getDescription());
        wishlistItem.setAmountLocal(dto.getAmountLocal());
        wishlistItem.setAmountHome(dto.getAmountHome());
        wishlistItem.setCategory(dto.getCategory());
        wishlistItem.setPurchased(false);
        wishlistItem.setTrip(trip);
        return wishlistItem;
    }

    public void updateEntityFromDto(WishlistItemUpdateDto dto, WishlistItem wishlistItem) {
        if (dto.getDescription() != null) { wishlistItem.setDescription(dto.getDescription()); }
        if (dto.getAmountLocal() != null) { wishlistItem.setAmountLocal(dto.getAmountLocal()); }
        if (dto.getAmountHome() != null) { wishlistItem.setAmountHome(dto.getAmountHome()); }
        if (dto.getPurchased() != null) { wishlistItem.setPurchased(dto.getPurchased()); }
        if (dto.getCategory() != null) { wishlistItem.setCategory(dto.getCategory()); }
    }
}
