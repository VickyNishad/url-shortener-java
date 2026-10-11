package com.vickynishad.tech.dto.response;

public record CreateShortUrlResponse(
        String shortCode,
        String shortUrl
) {
}