package UrlShortener.service;

import UrlShortener.model.UrlMapping;

import UrlShortener.model.User;

import UrlShortener.repository.UrlRepository;

import UrlShortener.repository.UserRepository;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.time.ZoneId;

import java.util.List;

import java.util.Random;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    private final UserRepository userRepository;

    public UrlService(

            UrlRepository urlRepository,

            UserRepository userRepository

    ) {

        this.urlRepository =
                urlRepository;

        this.userRepository =
                userRepository;
    }

    // Generate Short URL
    public String shortenUrl(

        String originalUrl,

        String customAlias,

        Integer expiryHours

){

        // Logged-in Username
        Authentication auth =
                SecurityContextHolder
                    .getContext()
                    .getAuthentication();

        String username =
                auth.getName();

        // Find User
        User user =
                userRepository
                    .findByUsername(
                        username
                    )
                    .orElseThrow();

        // Generate Short Code
        String shortCode;

        // Use Custom Alias
                // Use Custom Alias
                if(
                customAlias != null
                &&
                !customAlias.isBlank()
                ){

                // Alias Already Exists
                if(
                        urlRepository
                        .findByShortCode(
                                customAlias
                        )
                        .isPresent()
                ){

                        throw new RuntimeException(
                        "Custom alias already exists"
                        );
                }

                shortCode =
                        customAlias;

                }else{

                shortCode =
                        generateShortCode();
                }
        // Create URL Mapping
        UrlMapping url =
                new UrlMapping();

        url.setOriginalUrl(
                originalUrl
        );

        url.setShortCode(
                shortCode
        );

        url.setClicks(0);
        // Expiry Time
        // Expiry Time
        if(expiryHours != null){
        
            url.setExpiryTime(
        
                LocalDateTime.now(
                    ZoneId.of("Asia/Kolkata")
                ).plusHours(expiryHours)
            );
        }
        // Assign Owner
        url.setUser(user);

        // Save
        urlRepository.save(url);

        return shortCode;
    }

    // Redirect URL
    public String getOriginalUrl(

            String shortCode

    ){

        UrlMapping url =
                urlRepository
                    .findByShortCode(
                        shortCode
                    )
                    .orElse(null);

        if(url == null){

            return null;
        }
        // Expired
        if(
        url.getExpiryTime() != null
        &&
        LocalDateTime.now()
                .isAfter(
                url.getExpiryTime()
                )
        ){

        return null;
        }

        // Increase Clicks
        url.setClicks(
                url.getClicks() + 1
        );

        urlRepository.save(url);

        return url.getOriginalUrl();
    }

    // User Analytics
    public List<UrlMapping>
    getUserUrls(){

        Authentication auth =
                SecurityContextHolder
                    .getContext()
                    .getAuthentication();

        String username =
                auth.getName();

        User user =
                userRepository
                    .findByUsername(
                        username
                    )
                    .orElseThrow();

        return urlRepository
                .findByUser(user);
    }

    // Delete URL
    public void deleteUrl(

            String shortCode

    ){

        UrlMapping url =
                urlRepository
                    .findByShortCode(
                        shortCode
                    )
                    .orElse(null);

        if(url != null){

            urlRepository.deleteById(url.getId());

        }
    }

    // Generate Random Code
    private String generateShortCode(){

        String chars =
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder shortCode =
                new StringBuilder();

        Random random =
                new Random();

        for(int i = 0; i < 6; i++){

            shortCode.append(
                    chars.charAt(
                        random.nextInt(
                            chars.length()
                        )
                    )
            );
        }

        return shortCode.toString();
    }
}
