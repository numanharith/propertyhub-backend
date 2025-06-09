package com.propertyhub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Lob // For potentially long descriptions
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "location", nullable = false)
    private String location; // Full address

    @Column(name = "location_lat", precision = 9, scale = 6)
    private BigDecimal locationLat;

    @Column(name = "location_lon", precision = 9, scale = 6)
    private BigDecimal locationLon;

    @Column(name = "property_type", nullable = false)
    private String propertyType; // e.g., 'Condo', 'HDB Flat', 'House'

    @Column(name = "listing_type", nullable = false)
    private String listingType; // e.g., 'For Sale', 'For Rent'

    private Integer bedrooms;

    private Integer bathrooms;

    @Column(name = "size_sqft")
    private Integer sizeSqft;

    @Column(nullable = false, length = 50)
    private String status = "active"; // e.g., 'active', 'pending', 'sold', 'rented'

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    private Boolean featured = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lister_id", nullable = false)
    private User lister;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Added CascadeType.PERSIST and CascadeType.MERGE
    @JoinTable(
        name = "property_amenities",
        joinColumns = @JoinColumn(name = "property_id"),
        inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }

    // Helper methods to maintain bidirectional relationships
    public void addImage(Image image) {
        images.add(image);
        image.setProperty(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setProperty(null);
    }

    public void addAmenity(Amenity amenity) {
        amenities.add(amenity);
        amenity.getProperties().add(this);
    }

    public void removeAmenity(Amenity amenity) {
        amenities.remove(amenity);
        amenity.getProperties().remove(this);
    }

    // Constructors, getters, setters, etc. are handled by Lombok
}
