# PropertyHub API Postman Collection

This repository contains a Postman collection for testing and interacting with the PropertyHub API. The collection includes all available endpoints organized by functionality.

## Contents

1. [Getting Started](#getting-started)
2. [Authentication](#authentication)
3. [Properties](#properties)
4. [Users](#users)
5. [Environment Variables](#environment-variables)

## Getting Started

### Prerequisites

- [Postman](https://www.postman.com/downloads/) installed on your machine
- PropertyHub backend server running (default: http://localhost:8080)

### Importing the Collection

1. Open Postman
2. Click on "Import" button in the top left corner
3. Select "Upload Files" and choose both:
   - `PropertyHub.postman_collection.json`
   - `PropertyHub.postman_environment.json`
4. Click "Import"
5. Select the "PropertyHub Environment" from the environment dropdown in the top right corner

## Authentication

The API uses JWT (JSON Web Token) for authentication. The collection includes scripts to automatically store the JWT token after login.

### Registration

1. Open the "Register User" request in the Authentication folder
2. Modify the request body with your desired user details
3. Send the request
4. You should receive a success message

Example request body:
```json
{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
}
```

### Login

1. Open the "Login User" request in the Authentication folder
2. Enter your email and password in the request body
3. Send the request
4. The response will include a JWT token, which will be automatically stored in the environment variables

Example request body:
```json
{
    "email": "test@example.com",
    "password": "password123"
}
```

## Properties

The Properties folder contains requests for managing property listings.

### Viewing Properties

- **Get All Properties**: Retrieves a paginated list of properties with optional filtering
- **Get Property by ID**: Retrieves detailed information about a specific property
- **Filter Properties**: Demonstrates how to use query parameters to filter properties

### Managing Properties (requires authentication with LISTER role)

- **Create Property**: Creates a new property listing
- **Update Property**: Updates an existing property (must be owned by the authenticated user)
- **Delete Property**: Deletes a property (must be owned by the authenticated user)

## Users

The Users folder contains requests for user-related operations.

- **Get Current User**: Retrieves details of the currently authenticated user

## Environment Variables

The collection uses the following environment variables:

- `base_url`: The base URL of the PropertyHub API (default: http://localhost:8080)
- `auth_token`: JWT token received after login (automatically set by the Login request)
- `user_id`: ID of the authenticated user (automatically set by the Login request)
- `username`: Username of the authenticated user (automatically set by the Login request)
- `user_role`: Role of the authenticated user (automatically set by the Login request)

These variables are used throughout the collection to simplify requests and maintain authentication state.

## Testing Workflow

1. Register a new user (or use an existing one)
2. Login to get a JWT token
3. Test the authenticated endpoints (Create Property, Update Property, etc.)
4. Test the public endpoints (Get All Properties, Get Property by ID, etc.)

## Notes

- The JWT token expires after 24 hours (as configured in the backend)
- Some operations require specific roles (e.g., LISTER role for property management)
- Property IDs in the example requests should be replaced with actual IDs from your database