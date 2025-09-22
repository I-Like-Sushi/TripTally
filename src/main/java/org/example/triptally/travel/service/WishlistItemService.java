package org.example.triptally.travel.service;

import org.example.triptally.exception.trip.TripNotFoundException;
import org.example.triptally.travel.currencyRates.CurrencyConversionService;
import org.example.triptally.travel.dto.wishlist.WishlistItemRequestDto;
import org.example.triptally.travel.mapper.WishlistItemMapper;
import org.example.triptally.travel.model.Trip;
import org.example.triptally.travel.model.WishlistItem;
import org.example.triptally.travel.repository.TripRepository;
import org.example.triptally.travel.repository.WishlistItemRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class WishlistItemService {

    private final WishlistItemMapper wishlistItemMapper;
    private final TripRepository tripRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final CurrencyConversionService currencyConversionService;

    public WishlistItemService(WishlistItemMapper wishlistItemMapper,
                               TripRepository tripRepository,
                               WishlistItemRepository wishlistItemRepository,
                               CurrencyConversionService currencyConversionService) {
        this.wishlistItemMapper = wishlistItemMapper;
        this.tripRepository = tripRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.currencyConversionService = currencyConversionService;
    }

    public WishlistItem createWishlistItem(WishlistItemRequestDto dto, String tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found."));

        WishlistItem wishlistItem = wishlistItemMapper.toEntity(dto, trip);
        wishlistItem.setId(UUID.randomUUID().toString().replace("-", "").substring(0, 9));

        currencyConversionService.fillMissingAmounts(
                trip,
                dto.getAmountLocal(),
                dto.getAmountHome(),
                wishlistItem::setAmountLocal,
                wishlistItem::setAmountHome
        );

        return wishlistItemRepository.save(wishlistItem);
    }

}

