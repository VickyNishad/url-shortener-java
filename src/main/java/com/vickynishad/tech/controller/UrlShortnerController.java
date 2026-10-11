package com.vickynishad.tech.controller;

import com.vickynishad.tech.dto.request.CreateShortUrlRequest;
import com.vickynishad.tech.service.UrlShortnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class UrlShortnerController {

    private final UrlShortnerService urlShortnerService;

    @PostMapping("/api/v1/url/short")
    public String urlShort(@Valid @RequestBody CreateShortUrlRequest request){
        return urlShortnerService.generateShortUrl(request);
    }

    @GetMapping("/{shortCode}")
    public org.springframework.http.ResponseEntity<Void> redirect(
            @PathVariable String shortCode) {

        String originalUrl = urlShortnerService.getOriginalUrl(shortCode);

        return org.springframework.http.ResponseEntity
                .status(org.springframework.http.HttpStatus.FOUND)
                .location(java.net.URI.create(originalUrl))
                .build();
    }
}
