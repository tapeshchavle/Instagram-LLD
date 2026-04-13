package com.instagram.feed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Feed Service - Generates personalized feeds using hybrid push/pull strategy.
 * Fan-out-on-write for normal users, fan-out-on-read for celebrities.
 * Runs independently on port 8083.
 */
@SpringBootApplication
public class FeedServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedServiceApplication.class, args);
    }
}
