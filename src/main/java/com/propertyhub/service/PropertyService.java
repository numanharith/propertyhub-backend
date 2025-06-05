package com.propertyhub.service;

import com.propertyhub.dto.property.PropertyDetailResponse;
import com.propertyhub.dto.property.PropertyRequest;
import com.propertyhub.dto.property.PropertySummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

public interface PropertyService {
    PropertyDetailResponse createProperty(PropertyRequest propertyRequest);
    PropertyDetailResponse getPropertyById(UUID propertyId);
    Page<PropertySummaryResponse> getAllProperties(Pageable pageable, Map<String, String> filters);
    Page<PropertySummaryResponse> getPropertiesByUserEmail(String email, Pageable pageable, Map<String, String> filters);
    PropertyDetailResponse updateProperty(UUID propertyId, PropertyRequest propertyRequest);
    void deleteProperty(UUID propertyId);
}
