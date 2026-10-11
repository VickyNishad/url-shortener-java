package com.vickynishad.tech.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateShortUrlRequest(

        @NotBlank(message = "Original URL is required")
        @Size(max = 2048, message = "URL must not exceed 2048 characters")
        String originalUrl

) {
}