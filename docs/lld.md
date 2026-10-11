# Low-Level Design — URL Shortener

## 1. Package Structure

com.vicky.urlshortener
├── controller
├── service
│   └── impl
├── repository
├── entity
├── dto
│   ├── request
│   └── response
├── exception
├── mapper
└── config

## 2. Main Classes

### UrlController

Handles HTTP requests and delegates business logic to UrlService.

### UrlService

Defines operations for URL creation, redirection and statistics.

### UrlServiceImpl

Implements the URL business rules.

### UrlRepository

Provides database access for short URL records.

### ShortUrl

Represents the persistent short URL entity.

### UrlClick

Represents an individual successful redirect event.

### CreateShortUrlRequest

Contains the original URL and optional expiry timestamp.

### ShortUrlResponse

Contains the short code, short URL and expiry information.

### UrlStatsResponse

Contains the short code and click count.

### GlobalExceptionHandler

Converts application exceptions into consistent API error responses.

## 3. Design Principles

* Controllers should not contain business logic.
* Services should not depend on HTTP request or response objects unnecessarily.
* Database entities should not be returned directly as API responses.
* Validation should happen at the API boundary and business-rule level.
* Dependencies should be injected through constructors.
* Repository responsibilities should remain focused on persistence.

## 4. Short-Code Generation

Generate a random URL-safe code of a defined length.

The database must enforce uniqueness. If a collision occurs, retry generation a bounded number of times before returning a controlled error.

## 5. Transaction Boundaries

URL creation should be transactional.

Click recording and redirect handling require careful design: a database failure while recording analytics should not accidentally produce an incorrect redirect or an unintended transaction rollback. The final policy will be decided during implementation.
