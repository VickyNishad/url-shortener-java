
package com.vickynishad.tech.service.impl;

import com.vickynishad.tech.dto.request.CreateShortUrlRequest;
import com.vickynishad.tech.entity.ShortUrl;
import com.vickynishad.tech.entity.UrlClick;
import com.vickynishad.tech.mapper.UrlShortnerMapper;
import com.vickynishad.tech.repository.ShortUrlRepository;
import com.vickynishad.tech.repository.UrlClickRepository;
import com.vickynishad.tech.service.UrlShortnerService;
import com.vickynishad.tech.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UrlShortnerServiceImpl implements UrlShortnerService {

    private static final int MAX_RETRIES = 5;

    private final ShortUrlRepository shortUrlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final UrlClickRepository urlClickRepository;

    @Value("${app.base-url:http://localhost:8080}") String baseUrl;

    @Override
    @Transactional
    public String generateShortUrl(CreateShortUrlRequest request) {

        validateUrl(request.originalUrl());

        for (int i = 0; i < MAX_RETRIES; i++) {

            String shortCode = shortCodeGenerator.generate();

            try {
                ShortUrl shortUrl =
                        UrlShortnerMapper.toEntity(shortCode, request.originalUrl());

                shortUrlRepository.saveAndFlush(shortUrl);

                return baseUrl+"/"+shortCode;

            } catch (DataIntegrityViolationException exception) {
                // A generated code may already exist.
                // Retry only when the short code caused the conflict.
                boolean codeExists =
                        shortUrlRepository.existsByShortCode(shortCode);

                if (!codeExists) {
                    throw exception;
                }
            }
        }

        throw new IllegalStateException(
                "Unable to generate a unique short code after retries."
        );
    }

    @Override
    public String getOriginalUrl(String code) {

        if (code == null || code.isBlank()){
            throw new IllegalArgumentException("Short code is required.");
        }


        ShortUrl shortUrl = shortUrlRepository.findByShortCode(code).orElseThrow(()-> new NoSuchElementException("Short URL not found."));

        if (!shortUrl.isActive()) {
            throw new IllegalStateException("Short URL is inactive.");
        }

//        if (shortUrl.getExpiresAt() != null
//                && !shortUrl.getExpiresAt().isAfter(java.time.LocalDateTime.now())) {
//            throw new IllegalStateException("Short URL has expired.");
//        }

        UrlClick click = new UrlClick(shortUrl);
        urlClickRepository.save(click);

        return shortUrl.getOriginalUrl();
    }

    private void validateUrl(String originalUrl) {

        if (originalUrl == null || originalUrl.isBlank()) {
            throw new IllegalArgumentException(
                    "Original URL must not be blank."
            );
        }

        try {
            URI uri = URI.create(originalUrl.trim());
            String scheme = uri.getScheme();

            if (uri.getHost() == null
                    || scheme == null
                    || !(scheme.equalsIgnoreCase("http")
                    || scheme.equalsIgnoreCase("https"))) {
                throw new IllegalArgumentException(
                        "URL must be a valid HTTP or HTTPS URL."
                );
            }

        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Please provide a valid HTTP or HTTPS URL.",
                    exception
            );
        }
    }
}