package org.example.eindopdrachtbackend.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public class UserUpdateDto {

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

    private List<String> allowViewingAccesTo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    private LocalDate dateOfBirth;

    private String gender;
    private String bio;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public List<String> getAllowViewingAccesTo() { return allowViewingAccesTo; }
    public void setAllowViewingAccesTo(List<String> allowViewingAccesTo) { this.allowViewingAccesTo = allowViewingAccesTo; }
    public void addAllowViewingAccesTo(String allowViewingAccesTo) { this.allowViewingAccesTo.add(allowViewingAccesTo); }
    public void removeAllowViewingAccesTo(String allowViewingAccesTo) { this.allowViewingAccesTo.remove(allowViewingAccesTo); }


}

