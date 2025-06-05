package com.propertyhub.dto.property;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PropertyRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotBlank(message = "Location is required")
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    private Double latitude;
    private Double longitude;

    @NotBlank(message = "Property type is required")
    private String propertyType;

    @NotBlank(message = "Listing type is required")
    private String listingType;

    private Integer bedrooms;
    private Double bathrooms;
    private Double areaSqFt;

    private List<String> amenities;

    @NotEmpty(message = "At least one image URL is required")
    private List<String> imageUrls;
}
