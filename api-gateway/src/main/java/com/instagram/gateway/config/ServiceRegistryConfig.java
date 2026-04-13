package com.instagram.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Central registry of downstream service URLs.
 */
@Component
public class ServiceRegistryConfig {

    @Value("${instagram.user-service.url}")
    private String userServiceUrl;

    @Value("${instagram.post-service.url}")
    private String postServiceUrl;

    @Value("${instagram.feed-service.url}")
    private String feedServiceUrl;

    @Value("${instagram.engagement-service.url}")
    private String engagementServiceUrl;

    @Value("${instagram.search-service.url}")
    private String searchServiceUrl;

    @Value("${instagram.notification-service.url}")
    private String notificationServiceUrl;

    public String getUserServiceUrl() { return userServiceUrl; }
    public String getPostServiceUrl() { return postServiceUrl; }
    public String getFeedServiceUrl() { return feedServiceUrl; }
    public String getEngagementServiceUrl() { return engagementServiceUrl; }
    public String getSearchServiceUrl() { return searchServiceUrl; }
    public String getNotificationServiceUrl() { return notificationServiceUrl; }
}
