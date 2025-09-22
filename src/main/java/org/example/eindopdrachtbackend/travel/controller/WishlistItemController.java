package org.example.eindopdrachtbackend.travel.controller;

import jakarta.validation.Valid;
import org.example.eindopdrachtbackend.auth.AuthValidationService;
import org.example.eindopdrachtbackend.travel.dto.wishlist.WishlistItemRequestDto;
import org.example.eindopdrachtbackend.travel.dto.wishlist.WishlistItemResponseDto;
import org.example.eindopdrachtbackend.travel.dto.wishlist.WishlistItemUpdateDto;
import org.example.eindopdrachtbackend.travel.mapper.WishlistItemMapper;
import org.example.eindopdrachtbackend.travel.model.WishlistItem;
import org.example.eindopdrachtbackend.travel.repository.WishlistItemRepository;
import org.example.eindopdrachtbackend.travel.service.WishlistItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users/{userId}/trips/{tripId}/wishlist-item")
@PreAuthorize("hasRole('USER')")
public class WishlistItemController {

    private final WishlistItemService wishlistItemService;
    private final AuthValidationService authValidationService;
    private final WishlistItemMapper wishlistItemMapper;
    private final WishlistItemRepository wishlistItemRepository;

    public WishlistItemController(WishlistItemService wishlistItemService, AuthValidationService authValidationService, WishlistItemMapper wishlistItemMapper, WishlistItemRepository wishlistItemRepository) {
        this.wishlistItemService = wishlistItemService;
        this.authValidationService = authValidationService;
        this.wishlistItemMapper = wishlistItemMapper;
        this.wishlistItemRepository = wishlistItemRepository;
    }

    @PostMapping
    public ResponseEntity<WishlistItemResponseDto> addToWishlist(@PathVariable String tripId,
                                                                 @PathVariable Long userId,
                                                                 Authentication auth,
                                                                 @Valid @RequestBody WishlistItemRequestDto dto) {
        authValidationService.validateSelfOrThrow(userId, auth);
        WishlistItem newItem = wishlistItemService.createWishlistItem(dto, tripId);
        WishlistItemResponseDto responseDto = wishlistItemMapper.toDto(newItem);

        URI location = URI.create("/api/users/" + userId + "/trips/" + tripId + "/expense/" + responseDto.getId());

        return ResponseEntity.created(location).body(responseDto);
    }

    @DeleteMapping("/{wishlistItemId}")
    public ResponseEntity<String> deleteWishlistItem(@PathVariable String wishlistItemId,
                                                     @PathVariable Long userId,
                                                     @PathVariable String tripId,
                                                     Authentication auth) {
        authValidationService.validateSelfOrThrow(userId, auth);

        WishlistItem wishlistItem = wishlistItemRepository.findById(wishlistItemId).orElseThrow();

        if (!wishlistItem.getTrip().getTripId().equals(tripId)) {
            throw new AccessDeniedException("Expense does not belong to this trip.");
        }

        wishlistItemRepository.delete(wishlistItem);

        return ResponseEntity.ok().body("Wishlist Item has been deleted.");


    }

    @PutMapping("/{wishlistItemId}")
    public ResponseEntity<WishlistItemResponseDto> updateWishlistItem(@PathVariable String wishlistItemId,
                                                                      @PathVariable Long userId,
                                                                      @PathVariable String tripId,
                                                                      @RequestBody WishlistItemUpdateDto dto,
                                                                      Authentication auth){
        authValidationService.validateSelfOrThrow(userId, auth);

        WishlistItem wishlistItem = wishlistItemRepository.findById(wishlistItemId).orElseThrow();

        if (!wishlistItem.getTrip().getTripId().equals(tripId)) {
            throw new AccessDeniedException("Expense does not belong to this trip.");
        }

        wishlistItemMapper.updateEntityFromDto(dto, wishlistItem);

        wishlistItemRepository.save(wishlistItem);
        return ResponseEntity.ok().body(wishlistItemMapper.toDto(wishlistItem));
    }

    @GetMapping("/{wishlistItemId}")
    public ResponseEntity<WishlistItemResponseDto> getWishlistItem(@PathVariable String wishlistItemId,
                                                                   @PathVariable Long userId,
                                                                   @PathVariable String tripId,
                                                                   Authentication auth){
        authValidationService.validateSelfOrThrow(userId, auth);

        WishlistItem wishlistItem = wishlistItemRepository.findById(wishlistItemId).orElseThrow();

        if (!wishlistItem.getTrip().getTripId().equals(tripId)) {
            throw new AccessDeniedException("Expense does not belong to this trip.");
        }

        return ResponseEntity.ok().body(wishlistItemMapper.toDto(wishlistItem));
    }


}
