package com.vickynishad.tech.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "url_clicks",
    indexes = {
        @Index(
            name = "idx_url_clicks_url_time",
            columnList = "short_url_id, clicked_at"
        )
    }
)
public class UrlClick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "short_url_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_url_clicks_short_url")
    )
    private ShortUrl shortUrl;

    @Column(
        name = "clicked_at",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private LocalDateTime clickedAt;

    protected UrlClick() {
    }

    public UrlClick(ShortUrl shortUrl) {
        this.shortUrl = shortUrl;
    }

    public Long getId() {
        return id;
    }

    public ShortUrl getShortUrl() {
        return shortUrl;
    }

    public LocalDateTime getClickedAt() {
        return clickedAt;
    }
}