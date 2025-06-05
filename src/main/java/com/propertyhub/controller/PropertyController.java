package com.propertyhub.controller;

import com.propertyhub.dto.property.PropertyDetailResponse;
import com.propertyhub.dto.property.PropertyRequest;
import com.propertyhub.dto.property.PropertySummaryResponse;
import com.propertyhub.security.jwt.JwtUtil;
import com.propertyhub.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final JwtUtil jwtUtil;

    @Autowired
    public PropertyController(PropertyService propertyService, JwtUtil jwtUtil) {
        this.propertyService = propertyService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    @PreAuthorize("hasRole('LISTER')")
    public ResponseEntity<PropertyDetailResponse> createProperty(@Valid @RequestBody PropertyRequest propertyRequest) {
        PropertyDetailResponse createdProperty = propertyService.createProperty(propertyRequest);
        return new ResponseEntity<>(createdProperty, HttpStatus.CREATED);
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyDetailResponse> getPropertyById(@PathVariable UUID propertyId) {
        PropertyDetailResponse property = propertyService.getPropertyById(propertyId);
        return ResponseEntity.ok(property);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<PropertySummaryResponse>> getAllProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort, // e.g. sort=price,asc sort=createdAt,desc
            @RequestParam Map<String, String> allFilters // Captures all other query params for filtering
    ) {
        // Construct Pageable with sorting
        // Example sort: "price,asc", "title,desc"
        Sort.Direction direction = Sort.Direction.fromString(sort.length > 1 ? sort[1] : "desc");
        Sort.Order order = new Sort.Order(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        // Remove pagination/sort params from filters map if they are present
        allFilters.remove("page");
        allFilters.remove("size");
        allFilters.remove("sort");

        Page<PropertySummaryResponse> properties = propertyService.getAllProperties(pageable, allFilters);
        return ResponseEntity.ok(properties);
    }


    @PutMapping("/{propertyId}")
    @PreAuthorize("hasRole('LISTER')") // Further ownership check is done in service layer
    public ResponseEntity<PropertyDetailResponse> updateProperty(@PathVariable UUID propertyId,
                                                               @Valid @RequestBody PropertyRequest propertyRequest) {
        PropertyDetailResponse updatedProperty = propertyService.updateProperty(propertyId, propertyRequest);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{propertyId}")
    @PreAuthorize("hasRole('LISTER')") // Further ownership check is done in service layer
    public ResponseEntity<Void> deleteProperty(@PathVariable UUID propertyId) {
        propertyService.deleteProperty(propertyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-token-email")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<PropertySummaryResponse>> getPropertiesByTokenEmail(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort,
            @RequestParam Map<String, String> allFilters
    ) {
        // Extract token from Authorization header
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix

        // Extract email from token
        String email = jwtUtil.extractUsername(token);

        // Construct Pageable with sorting
        Sort.Direction direction = Sort.Direction.fromString(sort.length > 1 ? sort[1] : "desc");
        Sort.Order order = new Sort.Order(direction, sort[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        // Remove pagination/sort params from filters map if they are present
        allFilters.remove("page");
        allFilters.remove("size");
        allFilters.remove("sort");

        Page<PropertySummaryResponse> properties = propertyService.getPropertiesByUserEmail(email, pageable, allFilters);
        return ResponseEntity.ok(properties);
    }
}
