
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UrlShortnerServiceImpl implements UrlShortnerService {

    private static final Logger log =
            LoggerFactory.getLogger(UrlShortnerServiceImpl.class);

    private static final int MAX_RETRIES = 5;

    private final ShortUrlRepository shortUrlRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final UrlClickRepository urlClickRepository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    @Transactional
    public String generateShortUrl(CreateShortUrlRequest request) {

        log.debug("Starting short URL generation request.");

        if (request == null) {
            log.warn("Short URL generation failed: request is null.");
            throw new IllegalArgumentException("Request must not be null.");
        }

        validateUrl(request.originalUrl());

        for (int i = 1; i <= MAX_RETRIES; i++) {

            String shortCode = shortCodeGenerator.generate();

            try {
                ShortUrl shortUrl =
                        UrlShortnerMapper.toEntity(
                                shortCode,
                                request.originalUrl()
                        );

                shortUrlRepository.saveAndFlush(shortUrl);

                String shortUrlValue = baseUrl + "/" + shortCode;

                log.info("Short URL created successfully. Attempt: {}", i);

                return shortUrlValue;

            } catch (DataIntegrityViolationException exception) {

                boolean codeExists =
                        shortUrlRepository.existsByShortCode(shortCode);

                if (!codeExists) {
                    log.error(
                            "Database constraint violation during URL creation."
                    );
                    throw exception;
                }

                log.warn(
                        "Generated short code collision detected. Attempt: {}",
                        i
                );
            }
        }

        log.error(
                "Short URL generation failed after {} attempts.",
                MAX_RETRIES
        );

        throw new IllegalStateException(
                "Unable to generate a unique short code after retries."
        );
    }

    @Override
    @Transactional
    public String getOriginalUrl(String code) {

        if (code == null || code.isBlank()) {
            log.warn("Redirect rejected because short code is blank.");
            throw new IllegalArgumentException("Short code is required.");
        }

        ShortUrl shortUrl = shortUrlRepository.findByShortCode(code)
                .orElseThrow(() -> {
                    log.debug("No URL found for the requested short code.");
                    return new NoSuchElementException("Short URL not found.");
                });

        if (!shortUrl.isActive()) {
            log.info("Redirect rejected because the URL is inactive.");
            throw new IllegalStateException("Short URL is inactive.");
        }

        // Add expiry validation here when expiry support is enabled.

        UrlClick click = new UrlClick(shortUrl);
        urlClickRepository.save(click);

        log.debug("Click recorded for a short URL.");

        return shortUrl.getOriginalUrl();
    }

    private void validateUrl(String originalUrl) {

        if (originalUrl == null || originalUrl.isBlank()) {
            log.warn("URL validation failed: original URL is blank.");
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

                log.warn("URL validation failed: unsupported or invalid URL.");
                throw new IllegalArgumentException(
                        "URL must be a valid HTTP or HTTPS URL."
                );
            }

        } catch (IllegalArgumentException exception) {
            log.debug("Original URL failed validation.");

            throw new IllegalArgumentException(
                    "Please provide a valid HTTP or HTTPS URL.",
                    exception
            );
        }
    }
}