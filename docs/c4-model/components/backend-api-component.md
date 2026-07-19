# Xchange C4: Backend API Component

```mermaid
C4Component
title Component diagram for Backend API - Auth Module

ContainerDb(relationalDb, "Relational Database", "PostgreSQL", "Stores persistent data: user credentials, profile information, and preferences.")
Container(mobile, "Mobile Application", "React Native / Expo", "Provides a cross-platform interface for users to track crypto assets and manage their profile.")
ContainerDb(cacheDb, "Cache & Session Store", "Redis", "Caches high-frequency coin data to reduce external API calls and stores Refresh Tokens.")

Container_Boundary(api, "Backend API (Monolith)") {
  
  Boundary(authService, "Auth Module") {
    Component(signInController, "Sign In Controller", "Spring REST Controller", "Endpoint for user authentication.")
    Component(signUpController, "Sign Up Controller", "Spring REST Controller", "Endpoint for user registration.")
    
    Component(signInService, "Sign In Service", "Spring Service", "Orchestrates authentication logic and verifies credentials.")
    Component(signUpService, "Sign Up Service", "Spring Service", "Handles user creation business logic.")
    Component(authTokenService, "Auth Token Service", "Spring Component", "Generates JWTs and manages Refresh Tokens.")
    
    Component(userRepository, "User Repository", "Spring Data Repository", "Abstracts database operations for User entities.")

    Rel(signInController, signInService, "Passes credentials to", "Method Call / DTO")
    Rel(signUpController, signUpService, "Passes registration data to", "Method Call / DTO")
    
    Rel(signInService, userRepository, "Finds user by email", "Method Call")
    Rel(signUpService, userRepository, "Saves new user", "Method Call")
    Rel(signInService, authTokenService, "Requests token generation", "Method Call")
  }
}

Rel(authTokenService, cacheDb, "Stores Refresh Token", "RESP/TCP")
Rel(userRepository, relationalDb, "Reads/Writes user data", "JDBC/TCP")

Rel(mobile, signInController, "Authenticates to obtain tokens", "JSON/HTTPS")
Rel(mobile, signUpController, "Sends user registration data", "JSON/HTTPS")
```