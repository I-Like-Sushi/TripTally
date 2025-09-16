package org.example.eindopdrachtbackend.travel.repository;

import org.example.eindopdrachtbackend.travel.model.Expense;
import org.example.eindopdrachtbackend.travel.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT t FROM Trip t LEFT JOIN FETCH t.expenses WHERE t.tripId = :tripId")
    Optional<Trip> findByTripIdWithExpenses(@Param("tripId") String tripId);

    @Query("SELECT e FROM Expense e JOIN FETCH e.trip WHERE e.id = :expenseId")
    Optional<Expense> findByIdWithTrip(@Param("expenseId") Long expenseId);


}
