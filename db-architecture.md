
# PropertyHub Database UML Diagram

Based on the POJOs in the codebase, here's a UML diagram representing the database tables architecture:

```mermaid
classDiagram
    class User {
        UUID id PK
        String fullName
        String email UK
        String passwordHash
        String phoneNumber
        String agencyName
        String avatarUrl
        Role role
        ZonedDateTime createdAt
        ZonedDateTime updatedAt
    }

    class Property {
        UUID id PK
        String title
        String description
        BigDecimal price
        String locationAddress
        BigDecimal locationLat
        BigDecimal locationLon
        String propertyType
        String listingType
        Integer bedrooms
        Integer bathrooms
        Integer sizeSqft
        String status
        Boolean featured
        UUID lister_id FK
        ZonedDateTime createdAt
        ZonedDateTime updatedAt
    }

    class Amenity {
        UUID id PK
        String name UK
    }

    class Image {
        UUID id PK
        UUID property_id FK
        String imageUrl
        String description
        Integer uploadOrder
        ZonedDateTime uploadedAt
    }

    class Inquiry {
        UUID id PK
        UUID property_id FK
        UUID sender_id FK
        UUID receiver_id FK
        String subject
        String messageText
        ZonedDateTime sentAt
        Boolean isRead
    }

    class SavedProperty {
        UUID id PK
        UUID user_id FK
        UUID property_id FK
        ZonedDateTime savedAt
    }

    class SavedSearch {
        UUID id PK
        UUID user_id FK
        String searchCriteria
        ZonedDateTime createdAt
    }

    class Role {
        <<enumeration>>
        ROLE_SEEKER
        ROLE_LISTER
        ROLE_ADMIN
    }

    class property_amenities {
        UUID property_id FK
        UUID amenity_id FK
    }

    User "1" -- "0..*" Property : lister
    User "1" -- "0..*" Inquiry : sender
    User "1" -- "0..*" Inquiry : receiver
    User "1" -- "0..*" SavedProperty
    User "1" -- "0..*" SavedSearch
    User "1" -- "1" Role
    
    Property "1" -- "0..*" Image
    Property "1" -- "0..*" Inquiry
    Property "1" -- "0..*" SavedProperty
    
    Property "0..*" -- "0..*" Amenity : property_amenities
```

## Table Relationships

1. **User**:
   - One-to-many relationship with Property (a user can list multiple properties)
   - One-to-many relationship with Inquiry (as both sender and receiver)
   - One-to-many relationship with SavedProperty (a user can save multiple properties)
   - One-to-many relationship with SavedSearch (a user can save multiple searches)
   - Has a Role (enum)

2. **Property**:
   - Many-to-one relationship with User (a property has one lister)
   - One-to-many relationship with Image (a property can have multiple images)
   - One-to-many relationship with Inquiry (a property can have multiple inquiries)
   - One-to-many relationship with SavedProperty (a property can be saved by multiple users)
   - Many-to-many relationship with Amenity (a property can have multiple amenities)

3. **Amenity**:
   - Many-to-many relationship with Property (an amenity can be associated with multiple properties)

4. **Image**:
   - Many-to-one relationship with Property (an image belongs to one property)

5. **Inquiry**:
   - Many-to-one relationship with Property (an inquiry is about one property)
   - Many-to-one relationship with User as sender (an inquiry is sent by one user)
   - Many-to-one relationship with User as receiver (an inquiry is received by one user)

6. **SavedProperty**:
   - Many-to-one relationship with User (a saved property belongs to one user)
   - Many-to-one relationship with Property (a saved property reference is to one property)
   - Has a unique constraint on the combination of user_id and property_id

7. **SavedSearch**:
   - Many-to-one relationship with User (a saved search belongs to one user)

This UML diagram represents the database schema of the PropertyHub application, showing all entities, their attributes, and the relationships between them.