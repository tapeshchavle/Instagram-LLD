package com.instagram.feed.client;

import com.instagram.common.dto.response.PostResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * REST client for communicating with User Service.
 * Fetches follower lists and celebrity status.
 */
@Component
public class UserServiceClient {

    private static final Logger log = LoggerFactory.getLogger(UserServiceClient.class);
    private final RestClient restClient;

    @Value("${instagram.user-service.url:http://localhost:8081}")
    private String userServiceUrl;

    public UserServiceClient() {
        this.restClient = RestClient.create();
    }

    public Set<String> getFollowerIds(String userId) {
        try {
            return restClient.get()
                    .uri(userServiceUrl + "/api/v1/users/" + userId + "/followers/ids")
                    .retrieve()
                    .body(new ParameterizedTypeReference<Set<String>>() {});
        } catch (Exception e) {
            log.warn("Failed to fetch followers for user {}: {}", userId, e.getMessage());
            return Collections.emptySet();
        }
    }

    public Set<String> getFollowingIds(String userId) {
        try {
            return restClient.get()
                    .uri(userServiceUrl + "/api/v1/users/" + userId + "/following/ids")
                    .retrieve()
                    .body(new ParameterizedTypeReference<Set<String>>() {});
        } catch (Exception e) {
            log.warn("Failed to fetch following for user {}: {}", userId, e.getMessage());
            return Collections.emptySet();
        }
    }

    public boolean isCelebrity(String userId) {
        try {
            Map<String, Boolean> result = restClient.get()
                    .uri(userServiceUrl + "/api/v1/users/" + userId + "/is-celebrity")
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Boolean>>() {});
            return result != null && Boolean.TRUE.equals(result.get("celebrity"));
        } catch (Exception e) {
            log.warn("Failed to check celebrity status for user {}: {}", userId, e.getMessage());
            return false;
        }
    }
}
