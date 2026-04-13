package com.instagram.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Post Service - Manages post creation, media metadata, and hashtag extraction.
 * Publishes events to Feed, Search, and Notification services via Observer pattern.
 * Runs independently on port 8082.
 */
@SpringBootApplication
public class PostServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostServiceApplication.class, args);
    }
}
