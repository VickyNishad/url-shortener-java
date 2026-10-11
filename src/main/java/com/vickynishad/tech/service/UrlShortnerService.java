package com.vickynishad.tech.service;

import com.vickynishad.tech.dto.request.CreateShortUrlRequest;

public interface UrlShortnerService {

    public String generateShortUrl(CreateShortUrlRequest request);
    public String getOriginalUrl(String code);
}
