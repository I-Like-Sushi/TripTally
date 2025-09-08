package org.example.eindopdrachtbackend.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class UserRequestDto {

    @NotBlank
    @Size(min = 3, max = 30)
    private String username;
    @NotBlank
    @Size(min = 3, max = 30)
    private String firstName;
    @NotBlank
    @Size(min = 3, max = 30)
    private String lastName;
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 50, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>_\\-]).{8,}$",
            message = "Password must contain at least one uppercase, one lowercase, one digit, and one special character"
    )
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String gender;
    private String bio;

    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getBio() { return bio; }
    public String getGender() { return gender; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }

}

