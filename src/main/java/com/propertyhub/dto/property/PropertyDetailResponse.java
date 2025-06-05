package com.propertyhub.dto.property;

import com.propertyhub.dto.user.ListerInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDetailResponse {
    private UUID id;
    private String title;
    private String description;
    private Double price;
    private String location;
    private String address;
    private Double latitude;
    private Double longitude;
    private String propertyType;
    private String listingType;
    private Integer bedrooms;
    private Double bathrooms;
    private Double areaSqFt;
    private List<String> amenities;
    private List<String> imageUrls;
    private ListerInfo lister;
}
