package UrlShortener.controller;

import UrlShortener.model.UrlMapping;

import UrlShortener.model.UrlRequest;

import UrlShortener.service.UrlService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.net.URI;

import java.util.Collections;

import java.util.List;

import java.util.Map;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(
            UrlService urlService
    ) {

        this.urlService =
                urlService;
    }

    // Create Short URL
    @PostMapping("/api/shorten")
    public ResponseEntity<Map<String, String>>
    shortenUrl(

            @RequestBody
            UrlRequest request

    ){

        String shortCode =
                urlService.shortenUrl(

    request.getOriginalUrl(),

    request.getCustomAlias(),

    request.getExpiryHours()
);
        return ResponseEntity.ok(

                Collections.singletonMap(
                        "shortCode",
                        shortCode
                )
        );
    }

    // Redirect URL
    @GetMapping("/r/{shortCode}")
    public ResponseEntity<Void>
    redirectToOriginal(

            @PathVariable
            String shortCode

    ){

        String originalUrl =
                urlService.getOriginalUrl(
                        shortCode
                );

        if(originalUrl == null){

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .status(302)
                .location(
                    URI.create(
                        originalUrl
                    )
                )
                .build();
    }

    // User Analytics
    @GetMapping("/api/analytics")
    public List<UrlMapping>
    getAnalytics(){

        return urlService
                .getUserUrls();
    }

    // Delete URL
    @DeleteMapping(
        "/api/delete/{shortCode}"
    )
    public ResponseEntity<String>
    deleteUrl(

            @PathVariable
            String shortCode

    ){

        urlService.deleteUrl(
                shortCode
        );

        return ResponseEntity.ok(
                "URL Deleted Successfully"
        );
    }
}