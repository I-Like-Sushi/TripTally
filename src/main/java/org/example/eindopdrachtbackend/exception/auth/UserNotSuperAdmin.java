package org.example.eindopdrachtbackend.exception.auth;

public class UserNotSuperAdmin extends RuntimeException {
    public UserNotSuperAdmin(String message) {
        super(message);
    }
}
