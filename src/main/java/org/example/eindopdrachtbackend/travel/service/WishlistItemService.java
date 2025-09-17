package org.example.eindopdrachtbackend.travel.service;

import org.example.eindopdrachtbackend.exception.trip.TripNotFoundException;
import org.example.eindopdrachtbackend.travel.dto.wishlist.WishlistItemRequestDto;
import org.example.eindopdrachtbackend.travel.mapper.WishlistItemMapper;
import org.example.eindopdrachtbackend.travel.model.Trip;
import org.example.eindopdrachtbackend.travel.model.WishlistItem;
import org.example.eindopdrachtbackend.travel.repository.TripRepository;
import org.example.eindopdrachtbackend.travel.repository.WishlistItemRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WishlistItemService {

    private final WishlistItemMapper wishlistItemMapper;
    private final TripRepository tripRepository;
    private final WishlistItemRepository wishlistItemRepository;

    public WishlistItemService(WishlistItemMapper wishlistItemMapper,
                               TripRepository tripRepository,
                               WishlistItemRepository wishlistItemRepository) {
        this.wishlistItemMapper = wishlistItemMapper;
        this.tripRepository = tripRepository;
        this.wishlistItemRepository = wishlistItemRepository;
    }

    public WishlistItem createWishlistItem(WishlistItemRequestDto dto, String tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found."));

        String wishlistItemId = UUID.randomUUID().toString().replace("-", "").substring(0, 9);

        WishlistItem wishlistItem = wishlistItemMapper.toEntity(dto, trip);
        wishlistItem.setId(wishlistItemId);

        return wishlistItemRepository.save(wishlistItem);
    }
}
