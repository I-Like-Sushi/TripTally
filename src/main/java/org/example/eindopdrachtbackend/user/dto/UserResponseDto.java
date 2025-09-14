package org.example.eindopdrachtbackend.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserResponseDto {

    private Long id;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    private LocalDate dateOfBirth;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime accountCreatedAt;

    @NotEmpty
    private List<String> roles;

    private List<String> allowViewingAccesTo;

    private String gender;
    private String bio;
    private boolean enabled;

    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getFirstName() { return firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getLastName() { return lastName; }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }

    public void setBio(String bio) { this.bio = bio; }
    public String getBio() { return bio; }

    public void setGender(String gender) {this.gender = gender; }
    public String getGender() { return gender; }

    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }

    public void setRoles(List<String> roles) { this.roles = roles; }
    public List<String> getRoles() { return roles; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean getEnabled() { return enabled; }

    public List<String> getAllowViewingAccesTo() { return allowViewingAccesTo; }
    public void setAllowViewingAccesTo(List<String> allowViewingAccesTo) { this.allowViewingAccesTo = allowViewingAccesTo; }
    public void addAllowViewingAccesTo(String allowViewingAccesTo) { this.allowViewingAccesTo.add(allowViewingAccesTo); }
    public void removeAllowViewingAccesTo(String allowViewingAccesTo) { this.allowViewingAccesTo.remove(allowViewingAccesTo); }

    public LocalDateTime getAccountCreatedAt() { return accountCreatedAt; }
    public void setAccountCreatedAt(LocalDateTime accountCreatedAt) { this.accountCreatedAt = accountCreatedAt; }

}


