package org.example.triptally.user;

import jakarta.persistence.*;
import org.example.triptally.travel.model.Trip;
import org.hibernate.annotations.CreationTimestamp;

import org.example.triptally.image.Image;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User{

    @Id
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime accountCreatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_allowed_access", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "allowed_username")
    private List<String> allowedAccesView = new ArrayList<>();

    private boolean enabled;
    private String gender;
    private String bio;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trip> trips = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();


    public User() {}

    public List<Trip> getTrips() { return trips; }

    public void setTrips(List<Trip> trips) {
        this.trips.clear();
        if (trips != null) {
            trips.forEach(this::addTrip);
        }
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
        trip.setUser(this);
    }

    public void removeTrip(Trip trip) {
        trips.remove(trip);
        trip.setUser(null);
    }

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

    public List<String> getAllowedAccesView() { return allowedAccesView; }
    public void setAllowedAccesView(List<String> allowedAccesView) { this.allowedAccesView = allowedAccesView; }
    public void addAllowedAccesView(String username) {
        if (!this.allowedAccesView.contains(username)) {
            this.allowedAccesView.add(username);
        }
    }
    public void removeAllowedAccesView(String allowedAccesView) { this.allowedAccesView.remove(allowedAccesView); }

    public LocalDateTime getAccountCreatedAt() { return accountCreatedAt; }
    public void setAccountCreatedAt(LocalDateTime accountCreatedAt) { this.accountCreatedAt = accountCreatedAt; }


    public List<Image> getImages() { return images; }

    public void setImages(List<Image> images) {
        this.images.clear();
        if (images != null) {
            images.forEach(this::addImage);
        }
    }

    public void addImage(Image image) {
        images.add(image);
        image.setUser(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setUser(null);
    }
}
