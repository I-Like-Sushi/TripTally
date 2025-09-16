package org.example.eindopdrachtbackend.exception.trip;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(String message) {
        super(message);
    }
}
