
CREATE TABLE short_urls (
    id BIGINT NOT NULL AUTO_INCREMENT,
    short_code VARCHAR(16) NOT NULL,
    original_url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_short_urls PRIMARY KEY (id),
    CONSTRAINT uk_short_urls_short_code UNIQUE (short_code)
);

CREATE TABLE url_clicks (
    id BIGINT NOT NULL AUTO_INCREMENT,
    short_url_id BIGINT NOT NULL,
    clicked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_url_clicks PRIMARY KEY (id),
    CONSTRAINT fk_url_clicks_short_url
        FOREIGN KEY (short_url_id)
        REFERENCES short_urls(id),

    INDEX idx_url_clicks_url_time (short_url_id, clicked_at)
);