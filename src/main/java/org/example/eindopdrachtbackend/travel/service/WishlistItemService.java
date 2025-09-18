package org.example.eindopdrachtbackend.travel.service;

import org.example.eindopdrachtbackend.exception.trip.TripNotFoundException;
import org.example.eindopdrachtbackend.travel.currencyRates.CurrencyConversionService;
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

