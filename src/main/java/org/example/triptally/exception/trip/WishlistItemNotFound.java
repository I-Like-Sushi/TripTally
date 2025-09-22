package org.example.triptally.exception.trip;

public class WishlistItemNotFound extends RuntimeException {
    public WishlistItemNotFound(String message) {
        super(message);
    }
}
