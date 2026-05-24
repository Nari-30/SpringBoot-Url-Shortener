package UrlShortener.repository;

import UrlShortener.model.UrlMapping;

import UrlShortener.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

public interface UrlRepository
        extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping>
    findByShortCode(String shortCode);

    List<UrlMapping>
    findByUser(User user);
}