package org.example.triptally.travel.repository;

import org.example.triptally.travel.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, String> {
    @Query("SELECT e FROM Expense e JOIN FETCH e.trip WHERE e.id = :wishlistItemId")
    Optional<WishlistItem> findByIdWithTrip(@Param("wishlistItemId") String wishlistItemId);

}
