package com.propertyhub.dto.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertySummaryResponse {
    private UUID id;
    private String title;
    private Double price;
    private String location;
    private String propertyType;
    private String listingType;
    private Integer bedrooms;
    private Double bathrooms;
    private Double areaSqFt;
    private String mainImageUrl; // URL of a primary image
    private UUID listerId; // UUID of the lister (User)
}
