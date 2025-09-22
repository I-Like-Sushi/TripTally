package org.example.triptally.exception.user;

public class UserNotSuperAdmin extends RuntimeException {
    public UserNotSuperAdmin(String message) {
        super(message);
    }
}
