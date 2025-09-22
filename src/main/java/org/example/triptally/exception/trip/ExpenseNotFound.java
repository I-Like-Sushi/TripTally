package org.example.triptally.exception.trip;

public class ExpenseNotFound extends RuntimeException {
    public ExpenseNotFound(String message) {
        super(message);
    }
}
