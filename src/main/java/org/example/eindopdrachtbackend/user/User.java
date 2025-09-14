package org.example.eindopdrachtbackend.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private LocalDate dateOfBirth;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private List<String> roles = new ArrayList<>();

    private List<String> allowViewingAccesTo = new ArrayList<>();

    private boolean enabled;
    private String gender;
    private String bio;

    public User() {};

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getGender() { return gender; }
    public void setGender(String gender) {this.gender = gender; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
    public void addRoles(String roles) { this.roles.add(roles); }
    public void removeRoles(String roles) { this.roles.remove(roles); }

    public List<String> getAllowViewingAccesTo() { return allowViewingAccesTo; }
    public void setAllowViewingAccesTo(List<String> allowViewingAccesTo) { this.allowViewingAccesTo = allowViewingAccesTo; }
    public void addAllowViewingAccesTo(String username) {
        if (!this.allowViewingAccesTo.contains(username)) {
            this.allowViewingAccesTo.add(username);
        }
    }
    public void removeAllowViewingAccesTo(String allowViewingAccesTo) { this.allowViewingAccesTo.remove(allowViewingAccesTo); }


}
