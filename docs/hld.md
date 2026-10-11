# High-Level Design — URL Shortener

## 1. Architecture

The application follows a modular monolith architecture.

Client → Controller → Service → Repository → MySQL

## 2. Main Components

### URL Controller

Accepts URL creation requests, handles redirect requests and exposes statistics endpoints.

### URL Service

Contains business logic for short-code generation, URL creation, expiry checks and click tracking.

### URL Repository

Handles persistence and retrieval of URL records.

### Click Analytics

Records successful redirect events and provides click statistics.

### MySQL

Stores short URL metadata and click records.

## 3. Create URL Flow

1. Client submits an original URL.
2. Controller validates the request.
3. Service generates a short code.
4. Repository persists the URL record.
5. Client receives the short URL.

## 4. Redirect Flow

1. Browser requests a short URL.
2. Controller extracts the short code.
3. Service looks up the corresponding record.
4. Service checks whether the URL exists, is active and has not expired.
5. A successful redirect is recorded.
6. Server returns an HTTP redirect with the destination in the Location header.

## 5. Error Handling

* Invalid URL: HTTP 400
* Unknown short code: HTTP 404
* Expired or inactive URL: HTTP 410, according to the API contract
* Unexpected server failure: HTTP 500

## 6. Future Improvements

* Redis caching
* Rate limiting
* Asynchronous click analytics
* Monitoring and alerting
* Horizontal scaling
