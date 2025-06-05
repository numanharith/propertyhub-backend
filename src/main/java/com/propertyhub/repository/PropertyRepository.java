package com.propertyhub.repository;

import com.propertyhub.model.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID>, JpaSpecificationExecutor<Property> {
    // JpaSpecificationExecutor allows for dynamic queries using Specifications API
    // Example: Page<Property> findAllByListerId(UUID listerId, Pageable pageable);
    // Basic CRUD methods are inherited from JpaRepository
}
