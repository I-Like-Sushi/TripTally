package org.example.eindopdrachtbackend.exception.auth;

public class UserNotAdmin extends RuntimeException {
    public UserNotAdmin(String message) {
        super(message);
    }
}
