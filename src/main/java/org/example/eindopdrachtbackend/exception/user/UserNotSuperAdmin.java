package org.example.eindopdrachtbackend.exception.user;

public class UserNotSuperAdmin extends RuntimeException {
    public UserNotSuperAdmin(String message) {
        super(message);
    }
}
