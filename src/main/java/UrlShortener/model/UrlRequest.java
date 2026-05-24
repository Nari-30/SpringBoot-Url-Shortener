package UrlShortener.model;

public class UrlRequest {

    private String originalUrl;

    private String customAlias;

    private Integer expiryHours;

    // Getter
    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getCustomAlias() {
        return customAlias;
    }

    // Setter
    public void setOriginalUrl(
            String originalUrl
    ) {
        this.originalUrl = originalUrl;
    }

    public void setCustomAlias(
            String customAlias
    ) {
        this.customAlias = customAlias;
    }
    public Integer getExpiryHours() {
        return expiryHours;
    }
    public void setExpiryHours(
        Integer expiryHours
    ) {
        this.expiryHours = expiryHours;
    }

}