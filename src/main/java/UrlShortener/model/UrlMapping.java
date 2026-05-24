package UrlShortener.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
public class UrlMapping {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private String originalUrl;

    @Column(unique = true)
    private String shortCode;

    private int clicks;
    
    @JsonFormat(
    pattern =
    "yyyy-MM-dd'T'HH:mm:ss"
    )
    private LocalDateTime expiryTime;

    // User Ownership
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(
            String originalUrl
    ) {
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(
            String shortCode
    ) {
        this.shortCode = shortCode;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(
            int clicks
    ) {
        this.clicks = clicks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(
            User user
    ) {
        this.user = user;
    }

    public LocalDateTime getExpiryTime() {
            return expiryTime;
        }

        public void setExpiryTime(
                LocalDateTime expiryTime
        ) {
            this.expiryTime = expiryTime;
        }
}
