package com.instagram.feed.client;

import com.instagram.common.dto.response.PostResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * REST client for communicating with Post Service.
 * Fetches post details for feed rendering.
 */
@Component
public class PostServiceClient {

    private static final Logger log = LoggerFactory.getLogger(PostServiceClient.class);
    private final RestClient restClient;

    @Value("${instagram.post-service.url:http://localhost:8082}")
    private String postServiceUrl;

    public PostServiceClient() {
        this.restClient = RestClient.create();
    }

    public PostResponse getPost(String postId) {
        try {
            return restClient.get()
                    .uri(postServiceUrl + "/api/v1/posts/" + postId)
                    .retrieve()
                    .body(PostResponse.class);
        } catch (Exception e) {
            log.warn("Failed to fetch post {}: {}", postId, e.getMessage());
            return null;
        }
    }
}
