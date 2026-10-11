
package com.vickynishad.tech.dto;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        boolean success,
        String message,
        String path,
        LocalDateTime timestamp
) {
}