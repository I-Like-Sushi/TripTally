package org.example.eindopdrachtbackend.exception.common;

import org.example.eindopdrachtbackend.exception.auth.ForbiddenAction;
import org.example.eindopdrachtbackend.exception.auth.InvalidLoginException;
import org.example.eindopdrachtbackend.exception.auth.UnauthorizedException;
import org.example.eindopdrachtbackend.exception.trip.TripNotFoundException;
import org.example.eindopdrachtbackend.exception.user.UserNotAdmin;
import org.example.eindopdrachtbackend.exception.user.UserNotSuperAdmin;
import org.example.eindopdrachtbackend.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<String> handleTripNotFoundException(TripNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<Map<String, String>> handleInvalidLogin(InvalidLoginException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(UserNotAdmin.class)
    public ResponseEntity<String> handleUserNotAdmin(UserNotAdmin ex){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UserNotSuperAdmin.class)
    public ResponseEntity<String> handleUserNotSuperAdmin(UserNotSuperAdmin ex){
        return  ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenAction.class)
    public ResponseEntity<String> handleForbiddenAction(ForbiddenAction ex){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class) // catches all Exceptions
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong: " + ex.getMessage());
    }
}

