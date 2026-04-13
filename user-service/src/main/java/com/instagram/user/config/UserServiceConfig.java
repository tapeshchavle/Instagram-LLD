package com.instagram.user.config;

import org.springframework.stereotype.Component;

/**
 * Configuration for User Service.
 * Contains tunable parameters for celebrity detection and rate limiting.
 */
@Component
public class UserServiceConfig {

    /**
     * Users with follower count >= this threshold are treated as celebrities.
     * Celebrity posts use fan-out-on-read strategy instead of fan-out-on-write.
     */
    private long celebrityThreshold = 100_000;

    public long getCelebrityThreshold() { return celebrityThreshold; }
    public void setCelebrityThreshold(long celebrityThreshold) { this.celebrityThreshold = celebrityThreshold; }
}
