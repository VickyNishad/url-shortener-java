package com.vickynishad.tech.repository;

import com.vickynishad.tech.entity.UrlClick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlClickRepository extends JpaRepository<UrlClick,Long> {
}
