package com.propertyhub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "agency_name")
    private String agencyName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private Role role = Role.ROLE_SEEKER; // Default to seeker

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @OneToMany(mappedBy = "lister", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Property> properties = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Inquiry> sentInquiries = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Inquiry> receivedInquiries = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SavedProperty> savedProperties = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SavedSearch> savedSearches = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }

    // Helper methods to maintain bidirectional relationships
    public void addProperty(Property property) {
        properties.add(property);
        property.setLister(this);
    }

    public void removeProperty(Property property) {
        properties.remove(property);
        property.setLister(null);
    }

    public void addSavedProperty(SavedProperty savedProperty) {
        savedProperties.add(savedProperty);
        savedProperty.setUser(this);
    }

    public void removeSavedProperty(SavedProperty savedProperty) {
        savedProperties.remove(savedProperty);
        savedProperty.setUser(null);
    }

    public void addSavedSearch(SavedSearch savedSearch) {
        savedSearches.add(savedSearch);
        savedSearch.setUser(this);
    }

    public void removeSavedSearch(SavedSearch savedSearch) {
        savedSearches.remove(savedSearch);
        savedSearch.setUser(null);
    }

    // Constructors, getters, setters, etc. are handled by Lombok
}
