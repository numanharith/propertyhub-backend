package com.propertyhub.service.impl;

import com.propertyhub.dto.property.PropertyDetailResponse;
import com.propertyhub.dto.property.PropertyRequest;
import com.propertyhub.dto.property.PropertySummaryResponse;
import com.propertyhub.dto.user.ListerInfo;
import com.propertyhub.exception.ResourceNotFoundException;
import com.propertyhub.exception.UnauthorizedException;
import com.propertyhub.model.Amenity;
import com.propertyhub.model.Image;
import com.propertyhub.model.Property;
import com.propertyhub.model.User;
import com.propertyhub.model.Role;
import com.propertyhub.repository.PropertyRepository;
import com.propertyhub.repository.UserRepository;
import com.propertyhub.service.PropertyService;
import com.propertyhub.specification.PropertySpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Autowired
    public PropertyServiceImpl(PropertyRepository propertyRepository, UserRepository userRepository) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByEmail(username) // Assuming email is used as username in UserDetails
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
    }

    private void checkOwnership(Property property, User currentUser) {
        if (!property.getLister().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("User not authorized to modify this property.");
        }
    }

    @Override
    @Transactional
    public PropertyDetailResponse createProperty(PropertyRequest propertyRequest) {
        User currentUser = getCurrentAuthenticatedUser();

        // Ensure current user has LISTER role
        if (currentUser.getRole() != Role.ROLE_LISTER) {
            throw new UnauthorizedException("User must have LISTER role to create properties.");
        }

        Property property = new Property();
        mapRequestToEntity(propertyRequest, property);
        property.setLister(currentUser);

        Property savedProperty = propertyRepository.save(property);
        return mapEntityToDetailResponse(savedProperty);
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyDetailResponse getPropertyById(UUID propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property", "id", propertyId));
        return mapEntityToDetailResponse(property);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertySummaryResponse> getAllProperties(Pageable pageable, Map<String, String> filters) {
        Specification<Property> spec = PropertySpecification.fromFilterMap(filters);
        Page<Property> propertiesPage = propertyRepository.findAll(spec, pageable);
        return propertiesPage.map(this::mapEntityToSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertySummaryResponse> getPropertiesByUserEmail(String email, Pageable pageable, Map<String, String> filters) {
        Specification<Property> spec = PropertySpecification.fromFilterMap(filters);
        Page<Property> propertiesPage = propertyRepository.findAllByLister_Email(email, pageable);
        return propertiesPage.map(this::mapEntityToSummaryResponse);
    }

    @Override
    @Transactional
    public PropertyDetailResponse updateProperty(UUID propertyId, PropertyRequest propertyRequest) {
        User currentUser = getCurrentAuthenticatedUser();
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property", "id", propertyId));

        checkOwnership(property, currentUser);

        mapRequestToEntity(propertyRequest, property);
        Property updatedProperty = propertyRepository.save(property);
        return mapEntityToDetailResponse(updatedProperty);
    }

    @Override
    @Transactional
    public void deleteProperty(UUID propertyId) {
        User currentUser = getCurrentAuthenticatedUser();
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property", "id", propertyId));

        checkOwnership(property, currentUser);
        propertyRepository.delete(property);
    }

    // Helper methods for mapping
    private void mapRequestToEntity(PropertyRequest request, Property entity) {
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setPrice(new BigDecimal(request.getPrice().toString()));
        entity.setLocation(request.getLocation() + (request.getAddress() != null ? ", " + request.getAddress() : ""));

        if (request.getLatitude() != null) {
            entity.setLocationLat(new BigDecimal(request.getLatitude().toString()));
        }

        if (request.getLongitude() != null) {
            entity.setLocationLon(new BigDecimal(request.getLongitude().toString()));
        }

        entity.setPropertyType(request.getPropertyType());
        entity.setListingType(request.getListingType());
        entity.setBedrooms(request.getBedrooms());

        if (request.getBathrooms() != null) {
            entity.setBathrooms(request.getBathrooms().intValue());
        }

        if (request.getAreaSqFt() != null) {
            entity.setSizeSqft(request.getAreaSqFt().intValue());
        }

        // Clear existing images and add new ones
        entity.getImages().clear();
        if (request.getImageUrls() != null) {
            for (int i = 0; i < request.getImageUrls().size(); i++) {
                Image image = new Image();
                image.setImageUrl(request.getImageUrls().get(i));
                image.setUploadOrder(i);
                entity.addImage(image);
            }
        }

        // Clear existing amenities and add new ones
        entity.getAmenities().clear();
        if (request.getAmenities() != null) {
            for (String amenityName : request.getAmenities()) {
                Amenity amenity = new Amenity();
                amenity.setName(amenityName);
                entity.addAmenity(amenity);
            }
        }
    }

    private PropertyDetailResponse mapEntityToDetailResponse(Property entity) {

        ListerInfo listerInfo = new ListerInfo(entity.getLister().getId(), entity.getLister().getFullName());

        // Extract location and address from locationAddress
        String location = entity.getLocation();
        String address = "";
        if (location != null && location.contains(", ")) {
            int commaIndex = location.indexOf(", ");
            address = location.substring(commaIndex + 2);
            location = location.substring(0, commaIndex);
        }

        // Convert BigDecimal to Double
        Double price = entity.getPrice() != null ? entity.getPrice().doubleValue() : null;
        Double latitude = entity.getLocationLat() != null ? entity.getLocationLat().doubleValue() : null;
        Double longitude = entity.getLocationLon() != null ? entity.getLocationLon().doubleValue() : null;
        Double bathrooms = entity.getBathrooms() != null ? entity.getBathrooms().doubleValue() : null;
        Double areaSqFt = entity.getSizeSqft() != null ? entity.getSizeSqft().doubleValue() : null;

        // Extract amenity names
        List<String> amenityNames = entity.getAmenities().stream()
                .map(Amenity::getName)
                .collect(Collectors.toList());

        // Extract image URLs
        List<String> imageUrls = entity.getImages().stream()
                .sorted((i1, i2) -> Integer.compare(i1.getUploadOrder(), i2.getUploadOrder()))
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        return new PropertyDetailResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                price,
                location,
                address,
                latitude,
                longitude,
                entity.getPropertyType(),
                entity.getListingType(),
                entity.getBedrooms(),
                bathrooms,
                areaSqFt,
                amenityNames,
                imageUrls,
                listerInfo
        );
    }

    private PropertySummaryResponse mapEntityToSummaryResponse(Property entity) {
        // Extract location from locationAddress
        String location = entity.getLocation();
        if (location != null && location.contains(", ")) {
            location = location.substring(0, location.indexOf(", "));
        }

        // Convert BigDecimal to Double
        Double price = entity.getPrice() != null ? entity.getPrice().doubleValue() : null;
        Double bathrooms = entity.getBathrooms() != null ? entity.getBathrooms().doubleValue() : null;
        Double areaSqFt = entity.getSizeSqft() != null ? entity.getSizeSqft().doubleValue() : null;

        // Get the main image URL (first image)
        String mainImageUrl = null;
        if (entity.getImages() != null && !entity.getImages().isEmpty()) {
            mainImageUrl = entity.getImages().stream()
                    .sorted(Comparator.comparingInt(Image::getUploadOrder))
                    .map(Image::getImageUrl)
                    .findFirst()
                    .orElse(null);
        }

        return new PropertySummaryResponse(
                entity.getId(),
                entity.getTitle(),
                price,
                location,
                entity.getPropertyType(),
                entity.getListingType(),
                entity.getBedrooms(),
                bathrooms,
                areaSqFt,
                mainImageUrl,
                entity.getLister().getId() // Add the lister's UUID
        );
    }
}
