package com.vickynishad.tech.mapper;

import com.vickynishad.tech.entity.ShortUrl;

import java.time.LocalDateTime;

public final class UrlShortnerMapper {
    private UrlShortnerMapper(){}

    public static ShortUrl toEntity(String shortCode,String originalUrl){
        return new ShortUrl(shortCode,originalUrl, LocalDateTime.now());
    }
}
