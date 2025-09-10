package org.example.eindopdrachtbackend.exception.auth;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String message) {
        super(message);
    }
}
