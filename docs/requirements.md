# URL Shortener — Requirements

## 1. Project Overview

The URL Shortener converts long URLs into short, shareable links and redirects users to the original destination.

## 2. Functional Requirements

* FR-01: Users can create a short URL from a valid HTTP or HTTPS URL.
* FR-02: Each short code must be unique.
* FR-03: Opening a valid short URL redirects to its original destination.
* FR-04: Unknown short codes return HTTP 404.
* FR-05: Expired short URLs cannot be used for redirection.
* FR-06: The system records successful redirect clicks.
* FR-07: Users can retrieve basic click statistics using a short code.
* FR-08: Invalid input returns a meaningful client error.

## 3. Non-Functional Requirements

* NFR-01: The application should use a layered architecture.
* NFR-02: Database operations must use appropriate transactions.
* NFR-03: Short codes must have a database-enforced uniqueness constraint.
* NFR-04: The application must not log sensitive request data unnecessarily.
* NFR-05: The code must be unit-testable.
* NFR-06: Database schema changes should be version-controlled.
* NFR-07: The system should support future caching and horizontal scaling.

## 4. Initial Scope

Included:

* URL creation
* HTTP redirects
* Expiry support
* Click tracking
* Basic statistics
* Validation and exception handling

Not included in Version 1:

* User authentication
* Custom aliases
* QR code generation
* Advanced analytics dashboard
* Distributed microservices

## 5. Key Design Decisions

* Use Java 21 and Spring Boot.
* Use MySQL for persistent storage.
* Generate short codes on the backend.
* Enforce short-code uniqueness in the database.
* Use a modular monolith for the initial release.
* Introduce additional infrastructure only when justified by requirements.
