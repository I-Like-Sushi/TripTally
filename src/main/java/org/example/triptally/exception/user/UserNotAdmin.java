package org.example.triptally.exception.user;

public class UserNotAdmin extends RuntimeException {
    public UserNotAdmin(String message) {
        super(message);
    }
}
