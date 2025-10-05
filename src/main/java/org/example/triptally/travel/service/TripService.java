package org.example.triptally.travel.service;

import jakarta.transaction.Transactional;
import org.example.triptally.exception.trip.TripNotFoundException;
import org.example.triptally.exception.user.UserNotFoundException;
import org.example.triptally.travel.currencyRates.CurrencyValidator;
import org.example.triptally.travel.currencyRates.FxConfig;
import org.example.triptally.travel.currencyRates.FxRateRepository;
import org.example.triptally.travel.mapper.TripMapper;
import org.example.triptally.travel.dto.trip.TripRequestDto;
import org.example.triptally.travel.model.Trip;
import org.example.triptally.travel.repository.TripRepository;
import org.example.triptally.user.User;
import org.example.triptally.user.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripMapper tripMapper;
    private final FxRateRepository fxRateRepository;
    private final FxConfig fxConfig;

    public TripService(TripRepository tripRepository, UserRepository userRepository, TripMapper tripMapper, FxRateRepository fxRateRepository, FxConfig fxConfig) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.tripMapper = tripMapper;
        this.fxRateRepository = fxRateRepository;
        this.fxConfig = fxConfig;
    }

    @Transactional
    public Trip createTrip(Long userId, TripRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        CurrencyValidator.validateCurrencyCode(dto.getHomeCurrencyCode(), "home currency code");
        CurrencyValidator.validateCurrencyCode(dto.getLocalCurrencyCode(), "local currency code");

        Trip trip = tripMapper.toEntity(dto);
        trip.setUser(user);
        trip.setHomeCurrencyCode(dto.getHomeCurrencyCode());
        trip.setLocalCurrencyCode(dto.getLocalCurrencyCode());

        if (dto.getBudgetHomeCurrency() != null && dto.getBudgetLocalCurrency() == null) {
            // Convert home → local
            BigDecimal rate = fxRateRepository.findByBaseCurrencyAndQuoteCurrencyAndAsOfDate(
                            dto.getHomeCurrencyCode(),
                            dto.getLocalCurrencyCode(),
                            fxConfig.getSnapshotDate()
                    ).orElseThrow(() -> new IllegalStateException("No FX rate found"))
                    .getRate();

            trip.setBudgetHomeCurrency(dto.getBudgetHomeCurrency());
            trip.setBudgetLocalCurrency(dto.getBudgetHomeCurrency().multiply(rate));

        } else if (dto.getBudgetLocalCurrency() != null && dto.getBudgetHomeCurrency() == null) {
            // Convert local → home
            BigDecimal rate = fxRateRepository.findByBaseCurrencyAndQuoteCurrencyAndAsOfDate(
                            dto.getLocalCurrencyCode(),
                            dto.getHomeCurrencyCode(),
                            fxConfig.getSnapshotDate()
                    ).orElseThrow(() -> new IllegalStateException("No FX rate found"))
                    .getRate();

            trip.setBudgetLocalCurrency(dto.getBudgetLocalCurrency());
            trip.setBudgetHomeCurrency(dto.getBudgetLocalCurrency().multiply(rate));

        } else {
            if (dto.getBudgetHomeCurrency() == null && dto.getBudgetLocalCurrency() == null) {
                throw new IllegalArgumentException(
                        "You must provide either budgetHomeCurrency or budgetLocalCurrency"
                );
            }

            if (dto.getBudgetHomeCurrency() != null && dto.getBudgetLocalCurrency() != null) {
                BigDecimal expectedLocal = dto.getBudgetHomeCurrency()
                        .multiply(
                                fxRateRepository.findByBaseCurrencyAndQuoteCurrencyAndAsOfDate(
                                                dto.getHomeCurrencyCode(),
                                                dto.getLocalCurrencyCode(),
                                                fxConfig.getSnapshotDate()
                                        ).orElseThrow(() -> new IllegalStateException("No FX rate found"))
                                        .getRate()
                        );

                BigDecimal tolerance = new BigDecimal("0.01");
                if (dto.getBudgetLocalCurrency().subtract(expectedLocal).abs().compareTo(tolerance) > 0) {
                    throw new IllegalArgumentException(
                            "Provided budgets do not match the FX rate for " +
                                    dto.getHomeCurrencyCode() + " → " + dto.getLocalCurrencyCode()
                    );
                }

                trip.setBudgetHomeCurrency(dto.getBudgetHomeCurrency());
                trip.setBudgetLocalCurrency(dto.getBudgetLocalCurrency());
            }
        }

        String tripId = trip.getDestination().toUpperCase().replaceAll("\\s+", "_") + "_" +
                trip.getStartDate().toString().replaceAll("-", "") + "_" +
                UUID.randomUUID().toString().substring(0, 6);

        trip.setTripId(tripId);

        return tripRepository.save(trip);
    }

    @Transactional
    public Trip updateTrip(Long userId, String tripId, TripRequestDto dto) {
        Trip trip = tripRepository.findByTripId(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found."));

        if (!trip.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Trip does not belong to this user.");
        }

        String homeCurrency = dto.getHomeCurrencyCode() != null ? dto.getHomeCurrencyCode() : trip.getHomeCurrencyCode();
        String localCurrency = dto.getLocalCurrencyCode() != null ? dto.getLocalCurrencyCode() : trip.getLocalCurrencyCode();

        CurrencyValidator.validateCurrencyCode(homeCurrency, "home currency code");
        CurrencyValidator.validateCurrencyCode(localCurrency, "local currency code");

        tripMapper.updateEntityFromDto(dto, trip);

        trip.setHomeCurrencyCode(homeCurrency);
        trip.setLocalCurrencyCode(localCurrency);

        BigDecimal dtoBudgetHome = dto.getBudgetHomeCurrency();
        BigDecimal dtoBudgetLocal = dto.getBudgetLocalCurrency();

        if (dtoBudgetHome == null && dtoBudgetLocal == null) {
            return tripRepository.save(trip);
        }

        if (dtoBudgetHome != null && dtoBudgetLocal == null) {
            BigDecimal rate = fxRateRepository.findByBaseCurrencyAndQuoteCurrencyAndAsOfDate(
                            homeCurrency, localCurrency, fxConfig.getSnapshotDate()
                    ).orElseThrow(() -> new IllegalStateException("No FX rate found"))
                    .getRate();

            trip.setBudgetHomeCurrency(dtoBudgetHome);
            trip.setBudgetLocalCurrency(dtoBudgetHome.multiply(rate));
            return tripRepository.save(trip);
        }

        if (dtoBudgetLocal != null && dtoBudgetHome == null) {
            BigDecimal rate = fxRateRepository.findByBaseCurrencyAndQuoteCurrencyAndAsOfDate(
                            localCurrency, homeCurrency, fxConfig.getSnapshotDate()
                    ).orElseThrow(() -> new IllegalStateException("No FX rate found"))
                    .getRate();

            trip.setBudgetLocalCurrency(dtoBudgetLocal);
            trip.setBudgetHomeCurrency(dtoBudgetLocal.multiply(rate));
            return tripRepository.save(trip);
        }

        BigDecimal rate = fxRateRepository.findByBaseCurrencyAndQuoteCurrencyAndAsOfDate(
                homeCurrency, localCurrency, fxConfig.getSnapshotDate()
        ).orElseThrow(() -> new IllegalStateException("No FX rate found")).getRate();

        BigDecimal expectedLocal = dtoBudgetHome.multiply(rate);
        BigDecimal tolerance = new BigDecimal("0.01");

        if (dtoBudgetLocal.subtract(expectedLocal).abs().compareTo(tolerance) > 0) {
            throw new IllegalArgumentException(
                    "Provided budgets do not match the FX rate for " + homeCurrency + " → " + localCurrency
            );
        }

        trip.setBudgetHomeCurrency(dtoBudgetHome);
        trip.setBudgetLocalCurrency(dtoBudgetLocal);

        return tripRepository.save(trip);
    }



}


