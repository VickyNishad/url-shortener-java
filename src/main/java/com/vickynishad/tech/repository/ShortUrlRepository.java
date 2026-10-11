package com.vickynishad.tech.repository;

import com.vickynishad.tech.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl,Long> {
    Optional<ShortUrl> findByShortCode(String shortCode);
    public boolean existsByShortCode(String shortCode);
}
