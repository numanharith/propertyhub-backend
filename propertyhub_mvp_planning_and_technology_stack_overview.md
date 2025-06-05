# PropertyHub MVP Initial Planning Document

This document synthesizes the findings from the initial research and planning phase for PropertyHub, a Minimum Viable Product (MVP) designed to replicate the core functionalities of a property listing platform like PropertyGuru. It serves as the foundational blueprint for initiating the development phase, outlining the core concept, defined MVP scope, chosen technology stack, design principles, data structure, high-level roadmap, and initial monetization approach.

## 1. Core Concept: PropertyHub

PropertyHub aims to be a user-friendly online platform connecting property listers (agents, sellers, landlords) with property seekers (buyers, renters) in a specific market (initially focusing on key features observed in the Singapore market like PropertyGuru).

-   **Core Features:** Listing properties (for sale and rent), searching and filtering listings by various criteria (location, type, price, bedrooms, bathrooms, size), viewing detailed property information (description, photos, features), and enabling direct communication between seekers and listers.
-   **Target Audience:** Individuals looking to buy or rent property, and real estate agents/property owners looking to list properties.
-   **Inferred Business Model:** Primarily a platform model, generating revenue through services offered to property listers (agents/owners) to gain visibility for their listings, mirroring the core approach of established players like PropertyGuru which heavily relies on agent subscriptions and premium listing features.

## 2. Minimum Viable Product (MVP) Definition

The MVP for PropertyHub focuses on the essential features required to create a functional marketplace loop between property listers and seekers.

-   **User Authentication and Account Management (Lister):** Allows listers to register, login, and manage their profile.
-   **Property Listing Creation:** Provides a form for authenticated listers to add detailed property information and photos.
-   **Basic Property Search Functionality:** Enables seekers to search by location and apply basic filters (listing type, property type, price range, bedrooms).
-   **Property Listing Display (Search Results):** Presents search results in a clear list/grid view with key property details and a primary photo.
-   **Property Detail Page:** Displays comprehensive information for a single property, including a photo gallery, full description, features, location (basic map integration), and lister contact info.
-   **Contact Lister Functionality:** Offers a simple mechanism (e.g., contact form) for seekers to initiate contact with the lister of a property.

These features form the core transactional capability of the platform, allowing users to list, find, view, and inquire about properties.

## 3. Technology Blueprint

Based on the comparative analysis of suitable stacks for a scalable real estate platform MVP by 2025, the recommended technology stack for PropertyHub is:

-   **Frontend:** **React**
    -   *Justification:* High performance for dynamic UIs, large developer community, component reusability, excellent for building a responsive web application aligning with UI/UX principles.
-   **Backend:** **Python with Django & Django REST Framework**
    -   *Justification:* Rapid development capabilities with Django's "batteries-included" philosophy (ORM, admin panel, security), strong community support, robust for building secure APIs, good for handling complex business logic and data integrity.
-   **Database:** **PostgreSQL**
    -   *Justification:* Robust, scalable, strong support for transactional data and complex relational queries necessary for property attributes and user management. Includes built-in features adaptable for geospatial data (PostGIS extension, though advanced map features are post-MVP).
-   **Search Engine:** **Elasticsearch**
    -   *Justification:* Crucial for providing fast, relevant, and scalable full-text search and complex filtering (e.g., keyword search, price/size range queries, potentially geo-spatial search post-MVP) over a large volume of property listings, complementing PostgreSQL's strengths. Integrated with the Django backend.
-   **Cloud Hosting:** **AWS, Google Cloud, or Azure**
    -   *Justification:* All offer robust infrastructure for scaling compute (EC2/Compute Engine/Virtual Machines), storage (S3/Cloud Storage/Blob Storage for images), managed databases (RDS/Cloud SQL for PostgreSQL, Elastic Cloud/managed Elasticsearch), and networking required for a performant and scalable web application. Choice depends on team expertise and specific feature priorities (e.g., AWS breadth, GCP pricing/data services, Azure Microsoft integration). AWS is a common and safe starting point due to its extensive ecosystem.

This stack balances rapid MVP development speed with scalability, performance (especially for search via Elasticsearch), maintainability, and security, positioning PropertyHub for future growth.

## 4. Design Vision: UI/UX Principles & Mockups Summary

The design of PropertyHub's MVP is guided by principles emphasizing user-centricity, trust, clarity, performance, and a mobile-first approach.

-   **User-Centricity & Trust:** Design prioritizes ease of use for both seekers (finding properties) and listers (adding/managing listings). Professional visuals and clear information presentation build trust.
-   **Clean & Intuitive UI:** Minimalistic design, clear typography, consistent use of color (primary green, secondary indigo) and iconography for easy navigation and information consumption. High-quality property images are central.
-   **Performance:** Design considers fast loading and responsiveness crucial for a data-heavy real estate platform.
-   **Mobile-First:** Core flows are optimized for mobile devices, scaling up for desktop.

### High-Fidelity Mockups Summary

The mockups provide a visual representation of the key MVP pages, embodying the UI/UX principles:

-   **Homepage (`index.html`):** Features a prominent header with logo and navigation, a large hero section with a clear value proposition, and a central search bar with key filters (Location, Property Type, Looking For). It also includes sections for featured properties and benefits of using the platform.
    -   *Illustrative Element:* PropertyHub Logo: ![PropertyHub Logo](assets/images/logo.svg)
-   **Search Results Page (`search_results.html`):** Displays a list of property cards based on search criteria. Includes a filter sidebar (Price Range, Bathrooms, Size, Keywords, Amenities) and a main search bar for refining results. Options to toggle between list and map view are present (map view is a placeholder in MVP). Each listing card presents key details (photo, title, location, price, beds, baths, size) and a "View Details" CTA.
    -   *Illustrative Element (List Card Snippet):* ![Property Placeholder 1](assets/images/property_placeholder_1.svg)
    -   *Illustrative Element (Map Placeholder):* ![Map Area Placeholder](assets/images/map_placeholder.svg)
-   **Property Details Page (`property_details.html`):** Dedicated page for a single property. Features a large image gallery at the top, followed by detailed information: title, location, price, key features (beds, baths, size), full description, amenities list, and a location map placeholder. A prominent "Contact Lister" section includes agent photo, name, agency, and options to call or email (via form). Includes a section for similar properties.
-   **User Registration/Login Page (`auth.html`):** A simple, focused page with forms for user login and registration (switching between states). Includes options for email/password authentication and a "Forgot Password" link. Designed with clear fields and prominent call-to-action buttons.
-   **User Dashboard (`dashboard.html`):** Designed primarily for Listers in MVP. Includes a persistent sidebar for navigation (Overview, My Listings, Add New Listing, Profile, Inquiries). The main content area updates based on selection, showing summaries (Overview), a table of existing listings (My Listings), a form for adding new listings (Add New Listing), a profile editing form (Profile), and a list of received inquiries (Inquiries).
    -   *Illustrative Element (Add Listing Form Photo Previews):* ![Property Placeholder 2](assets/images/property_placeholder_2.svg) ![Property Placeholder 3](assets/images/property_placeholder_3.svg)

The mockups visually translate the functional requirements and UI/UX principles into tangible interfaces for development.

## 5. Data Architecture: Preliminary Database Schema

The preliminary database schema is designed using PostgreSQL to support the MVP features, managing users, properties, images, amenities, inquiries, and saved lists.

```sql
-- Table for Users
-- Stores information about registered users, including their role.
CREATE TABLE users (
    id UUID PRIMARY KEY, -- Unique identifier for the user
    full_name VARCHAR(255) NOT NULL, -- User's full name (for listers and seekers)
    email VARCHAR(255) UNIQUE NOT NULL, -- User's email address (unique login identifier)
    password_hash VARCHAR(255) NOT NULL, -- Hashed password for security
    phone_number VARCHAR(50), -- Optional phone number
    agency_name VARCHAR(255), -- Agency name, primarily for listers
    avatar_url VARCHAR(255), -- URL to user's profile picture
    role VARCHAR(50) NOT NULL DEFAULT 'seeker', -- User role (e.g., 'seeker', 'lister', 'admin')
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- Timestamp of account creation
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP  -- Timestamp of last update
);

-- Indexing:
-- For authentication/login
CREATE INDEX idx_users_email ON users (email);
-- For querying users by role
CREATE INDEX idx_users_role ON users (role);


-- Table for Properties
-- Stores details about individual property listings.
CREATE TABLE properties (
    id UUID PRIMARY KEY, -- Unique identifier for the property
    lister_id UUID NOT NULL, -- Foreign key linking to the user who listed the property
    title VARCHAR(255) NOT NULL, -- Title of the property listing
    listing_type VARCHAR(50) NOT NULL, -- Type of listing (e.g., 'For Sale', 'For Rent')
    property_type VARCHAR(50) NOT NULL, -- Type of property (e.g., 'Condo', 'HDB Flat', 'House')
    description TEXT, -- Detailed description of the property
    price DECIMAL(18, 2) NOT NULL, -- Price or rental rate of the property
    bedrooms SMALLINT, -- Number of bedrooms
    bathrooms SMALLINT, -- Number of bathrooms
    size_sqft INTEGER, -- Built-up area in square feet
    location_address VARCHAR(255) NOT NULL, -- Full address of the property
    location_lat DECIMAL(9, 6), -- Latitude for map integration (optional for MVP, good for future)
    location_lon DECIMAL(9, 6), -- Longitude for map integration (optional for MVP, good for future)
    status VARCHAR(50) NOT NULL DEFAULT 'active', -- Listing status (e.g., 'active', 'pending', 'sold', 'rented')
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- Timestamp of listing creation
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,  -- Timestamp of last update
    featured BOOLEAN DEFAULT FALSE -- Flag for featured properties (used on homepage)
);

-- Foreign Key Constraint:
ALTER TABLE properties
ADD CONSTRAINT fk_properties_lister_id
FOREIGN KEY (lister_id)
REFERENCES users (id)
ON DELETE CASCADE; -- If a user is deleted, their properties are also deleted.

-- Indexing:
-- For filtering/searching by location, type, status
CREATE INDEX idx_properties_location_type_status ON properties (location_address, property_type, listing_type, status);
-- For price range filtering
CREATE INDEX idx_properties_price ON properties (price);
-- For bedroom count filtering
CREATE INDEX idx_properties_bedrooms ON properties (bedrooms);
-- For size filtering
CREATE INDEX idx_properties_size_sqft ON properties (size_sqft);
-- For sorting by date
CREATE INDEX idx_properties_created_at ON properties (created_at DESC);
-- For retrieving a lister's properties
CREATE INDEX idx_properties_lister_id ON properties (lister_id);
-- For featured properties query
CREATE INDEX idx_properties_featured ON properties (featured) WHERE featured IS TRUE;


-- Table for Images
-- Stores image URLs associated with properties.
CREATE TABLE images (
    id UUID PRIMARY KEY, -- Unique identifier for the image
    property_id UUID NOT NULL, -- Foreign key linking to the property the image belongs to
    image_url VARCHAR(255) NOT NULL, -- URL of the image file (e.g., on S3)
    description VARCHAR(255), -- Optional description for the image
    upload_order INTEGER DEFAULT 0, -- Order to display images (e.g., for galleries)
    uploaded_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP -- Timestamp of image upload
);

-- Foreign Key Constraint:
ALTER TABLE images
ADD CONSTRAINT fk_images_property_id
FOREIGN KEY (property_id)
REFERENCES properties (id)
ON DELETE CASCADE; -- If a property is deleted, its images are also deleted.

-- Indexing:
-- To quickly retrieve all images for a property, ordered
CREATE INDEX idx_images_property_id_order ON images (property_id, upload_order);


-- Table for Amenities (Lookup Table)
-- Stores a predefined list of possible amenities.
CREATE TABLE amenities (
    id UUID PRIMARY KEY, -- Unique identifier for the amenity
    name VARCHAR(100) UNIQUE NOT NULL -- Name of the amenity (e.g., 'Pool', 'Gym', 'Parking')
);

-- Indexing:
-- For quick lookup by name
CREATE INDEX idx_amenities_name ON amenities (name);


-- Table for Property_Amenities (Join Table)
-- Links Properties to Amenities (Many-to-Many relationship).
CREATE TABLE property_amenities (
    property_id UUID NOT NULL, -- Foreign key to Properties table
    amenity_id UUID NOT NULL, -- Foreign key to Amenities table
    PRIMARY KEY (property_id, amenity_id) -- Composite primary key
);

-- Foreign Key Constraints:
ALTER TABLE property_amenities
ADD CONSTRAINT fk_prop_amenities_property_id
FOREIGN KEY (property_id)
REFERENCES properties (id)
ON DELETE CASCADE; -- If a property is deleted, its amenity links are deleted.

ALTER TABLE property_amenities
ADD CONSTRAINT fk_prop_amenities_amenity_id
FOREIGN KEY (amenity_id)
REFERENCES amenities (id)
ON DELETE CASCADE; -- If an amenity type is deleted, links are deleted (less common).


-- Table for Inquiries / Messages
-- Stores messages sent from potential buyers/renters to listers.
CREATE TABLE inquiries (
    id UUID PRIMARY KEY, -- Unique identifier for the inquiry
    property_id UUID NOT NULL, -- Foreign key to the property the inquiry is about
    sender_id UUID NOT NULL, -- Foreign key to the user who sent the inquiry (seeker)
    receiver_id UUID NOT NULL, -- Foreign key to the user who receives the inquiry (lister)
    subject VARCHAR(255), -- Subject of the inquiry
    message_text TEXT NOT NULL, -- The content of the message
    sent_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the message was sent
    is_read BOOLEAN DEFAULT FALSE -- Flag to track if the lister has read the message
);

-- Foreign Key Constraints:
ALTER TABLE inquiries
ADD CONSTRAINT fk_inquiries_property_id
FOREIGN KEY (property_id)
REFERENCES properties (id)
ON DELETE CASCADE; -- If the property is deleted, related inquiries might be kept or deleted based on policy. CASCADE for MVP simplicity.

ALTER TABLE inquiries
ADD CONSTRAINT fk_inquiries_sender_id
FOREIGN KEY (sender_id)
REFERENCES users (id)
ON DELETE SET NULL; -- If sender user is deleted, keep inquiry but anonymize sender.

ALTER TABLE inquiries
ADD CONSTRAINT fk_inquiries_receiver_id
FOREIGN KEY (receiver_id)
REFERENCES users (id)
ON DELETE CASCADE; -- If receiver user (lister) is deleted, their inquiries are deleted.

-- Indexing:
-- For retrieving inquiries for a specific lister (dashboard)
CREATE INDEX idx_inquiries_receiver_id ON inquiries (receiver_id);
-- For retrieving inquiries about a specific property
CREATE INDEX idx_inquiries_property_id ON inquiries (property_id);
-- For sorting inquiries by date
CREATE INDEX idx_inquiries_sent_at ON inquiries (sent_at DESC);


-- Table for Saved_Properties (Favorites)
-- Links users (seekers) to properties they have saved/favorited.
CREATE TABLE saved_properties (
    id UUID PRIMARY KEY, -- Unique identifier for the saved entry
    user_id UUID NOT NULL, -- Foreign key to the user who saved the property
    property_id UUID NOT NULL, -- Foreign key to the property that was saved
    saved_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the property was saved
    UNIQUE (user_id, property_id) -- Ensure a user can save a property only once
);

-- Foreign Key Constraints:
ALTER TABLE saved_properties
ADD CONSTRAINT fk_saved_properties_user_id
FOREIGN KEY (user_id)
REFERENCES users (id)
ON DELETE CASCADE; -- If user is deleted, their saved properties are deleted.

ALTER TABLE saved_properties
ADD CONSTRAINT fk_saved_properties_property_id
FOREIGN KEY (property_id)
REFERENCES properties (id)
ON DELETE CASCADE; -- If property is deleted, it's removed from saved lists.

-- Indexing:
-- To quickly retrieve all saved properties for a user
CREATE INDEX idx_saved_properties_user_id ON saved_properties (user_id);


-- Table for Saved_Searches
-- Stores search criteria saved by users.
CREATE TABLE saved_searches (
    id UUID PRIMARY KEY, -- Unique identifier for the saved search
    user_id UUID NOT NULL, -- Foreign key to the user who saved the search
    search_criteria JSONB NOT NULL, -- Stores search parameters (location, filters, etc.)
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP -- Timestamp when the search was saved
);

-- Foreign Key Constraint:
ALTER TABLE saved_searches
ADD CONSTRAINT fk_saved_searches_user_id
FOREIGN KEY (user_id)
REFERENCES users (id)
ON DELETE CASCADE; -- If user is deleted, their saved searches are deleted.

-- Indexing:
-- To quickly retrieve all saved searches for a user
CREATE INDEX idx_saved_searches_user_id ON saved_searches (user_id);

-- Note: For advanced full-text search on descriptions, titles, locations,
-- and potentially JSONB fields, PostgreSQL's built-in full-text search
-- or integration with Elasticsearch would be implemented separately
-- but relies on the data stored in these tables.
```

## 6. High-Level Project Roadmap (MVP)

The estimated timeline for delivering the PropertyHub MVP is **25 weeks**, structured into the following phases:

| Phase                                     | Estimated Duration | Key Activities                                                                                                                               | Key Deliverables                                                                                                                               |
| :---------------------------------------- | :----------------- | :------------------------------------------------------------------------------------------------------------------------------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------- |
| **1. Setup & Backend Foundation**         | 4 Weeks            | Environment Setup (Dev, Staging, Prod); CI/CD; Database Setup (PostgreSQL); Django Project Init; User Auth (Reg, Login, JWT); Basic User/Property APIs; Initial Search API. | Cloud environments ready; Git & CI/CD; Deployed DB; User & Basic Property APIs; Auth working. |
| **2. Frontend Core Development**          | 5 Weeks            | React Project Setup; Implement UI (Homepage, Auth, Dashboard Shell); Integrate Auth APIs; Basic Property Display UI; Core Layout & Routing. | Interactive Homepage; Functional Auth pages; Basic Dashboard UI; Reusable UI components. |
| **3. Advanced Features & Search Implementation** | 6 Weeks            | Refine DB Schema (indexing); Integrate Elasticsearch (Django); Develop Advanced Search API (filters, keywords); Implement Search Results UI; Integrate Search UI with API; Property Details Page UI; Basic Map Integration. | Functional Advanced Search API; Dynamic Search Results Page; Functional Property Details Page; Basic Map placeholder. |
| **4. Agent/Lister Features & Communication** | 5 Weeks            | Lister User Roles; Implement Lister Dashboard (My Listings); Property Listing Creation Form UI; Integrate Listing CRUD APIs; Implement Basic Inquiry System (Form, View). | Lister Dashboard functional; Property Creation/Editing forms; Basic Inquiry sending/receiving. |
| **5. Testing & Quality Assurance**        | 3 Weeks            | Unit Tests (F/B); Integration Testing; UI/UX Testing; Security Testing; User Acceptance Testing (UAT); Bug Fixing. | Test cases/results; Prioritized bug list; Stable MVP codebase; UAT feedback addressed. |
| **6. Deployment & Launch Preparation**    | 2 Weeks            | Prod Infrastructure Setup; Deploy Backend/Frontend; Configure DB/Elasticsearch for Prod; Setup Monitoring; Final Security Audit; Launch. | Prod environments ready; Deployed MVP app; Monitoring configured; Launched PropertyHub MVP. |

-   **Key Milestones:**
    -   End of Week 4: Backend API v1 & Preliminary DB Schema.
    -   End of Week 9: Core Frontend interactive & Auth integration.
    -   End of Week 15: Advanced Search & Property Details functional.
    -   End of Week 20: Lister Dashboard & Basic Inquiry system complete.
    -   End of Week 23: MVP codebase stable, tested, UAT feedback incorporated.
    -   End of Week 25: MVP deployed to production & Launched.

-   **Post-MVP Features (Future Considerations):** Advanced search filters, interactive map view, enhanced listing multimedia (floor plans, virtual tours), in-app messaging, seeker dashboard features (saved properties/searches), agent premium features (analytics, featured listings), financial tools, market insights, mobile applications, integrations with 3rd parties.

## 7. Monetization Strategy Analysis

Analyzing potential revenue models, particularly in the context of a competitive market dominated by agent-centric platforms like PropertyGuru, the most viable strategies for PropertyHub's MVP focus on the supply side (listers/agents).

-   **Recommended MVP Strategies:**
    1.  **Tiered Listing Limits:** Offer a **generous free tier** for basic individual listers or new agents to attract initial inventory. Introduce paid plans/subscriptions for agents or power users requiring a higher volume of listings.
        -   *Pros:* Simple, aligns with core platform function, attracts early supply with free option.
        -   *Cons:* Requires listing count tracking and payment integration (minor scope addition for MVP).
    2.  **Featured / Boosted Listings:** Allow listers to pay a small fee to highlight their listings for increased visibility in search results.
        -   *Pros:* Direct value proposition for listers, optional, lower barrier to entry than subscriptions, common marketplace pattern.
        -   *Cons:* Requires minor adjustments to search/display logic and payment integration.

-   **Rationale:** These two strategies provide multiple entry points for listers (free basic listings, paid for more listings, paid for more visibility) without requiring the full suite of agent tools found in mature platforms. They are feasible within the MVP roadmap timeframe, require minimal disruption to the seeker user flow, and directly monetize the core "listing" functionality.

-   **Future Monetization Strategies (Post-MVP):** Premium Agent Subscriptions (bundling advanced features), On-site Advertising (for third parties), Value-Added Services & Partnerships (e.g., mortgage referrals). These require more platform maturity, traffic, or complex integrations.

## Conclusion

This document outlines the fundamental components for the PropertyHub MVP, providing a clear scope, technical direction, design considerations, database structure, and development timeline. The focus on core features, a robust and scalable technology stack, user-centric design principles, and a targeted monetization approach for listers forms a solid foundation for building and launching a competitive property listing platform MVP. This plan now serves as the blueprint to move from planning to active development.