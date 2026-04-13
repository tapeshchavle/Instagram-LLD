package com.instagram.engagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Engagement Service - Manages likes, comments, and shares.
 * Uses Factory pattern for engagement creation, Decorator for content filtering.
 * Runs independently on port 8084.
 */
@SpringBootApplication
public class EngagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EngagementServiceApplication.class, args);
    }
}
