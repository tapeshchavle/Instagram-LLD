package com.instagram.gateway.controller;

import com.instagram.gateway.config.ServiceRegistryConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

/**
 * Gateway controller that proxies all requests to downstream services.
 * Acts as a Facade pattern — clients interact with a single URL.
 */
@RestController
@RequestMapping("/api/v1")
public class GatewayController {

    private final ServiceRegistryConfig registry;
    private final RestClient restClient;

    public GatewayController(ServiceRegistryConfig registry) {
        this.registry = registry;
        this.restClient = RestClient.create();
    }

    // ==================== User Routes ====================

    @RequestMapping(value = "/users/**", method = {RequestMethod.GET, RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> proxyUserService(
            @RequestHeader(value = "Content-Type", required = false) String contentType,
            jakarta.servlet.http.HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return proxy(registry.getUserServiceUrl(), request, body, contentType);
    }

    // ==================== Post Routes ====================

    @RequestMapping(value = "/posts/**", method = {RequestMethod.GET, RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> proxyPostService(
            @RequestHeader(value = "Content-Type", required = false) String contentType,
            jakarta.servlet.http.HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return proxy(registry.getPostServiceUrl(), request, body, contentType);
    }

    // ==================== Feed Routes ====================

    @RequestMapping(value = "/feed/**", method = {RequestMethod.GET})
    public ResponseEntity<String> proxyFeedService(
            @RequestHeader(value = "Content-Type", required = false) String contentType,
            jakarta.servlet.http.HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return proxy(registry.getFeedServiceUrl(), request, body, contentType);
    }

    // ==================== Search Routes ====================

    @RequestMapping(value = "/search/**", method = {RequestMethod.GET})
    public ResponseEntity<String> proxySearchService(
            @RequestHeader(value = "Content-Type", required = false) String contentType,
            jakarta.servlet.http.HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return proxy(registry.getSearchServiceUrl(), request, body, contentType);
    }

    // ==================== Notification Routes ====================

    @RequestMapping(value = "/notifications/**", method = {RequestMethod.GET, RequestMethod.PUT})
    public ResponseEntity<String> proxyNotificationService(
            @RequestHeader(value = "Content-Type", required = false) String contentType,
            jakarta.servlet.http.HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return proxy(registry.getNotificationServiceUrl(), request, body, contentType);
    }

    // ==================== Health Check ====================

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\": \"UP\", \"service\": \"api-gateway\"}");
    }

    // ==================== Proxy Logic ====================

    private ResponseEntity<String> proxy(String baseUrl,
                                          jakarta.servlet.http.HttpServletRequest request,
                                          String body,
                                          String contentType) {
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String targetUrl = baseUrl + path + (query != null ? "?" + query : "");

        try {
            var spec = switch (request.getMethod()) {
                case "GET" -> restClient.get().uri(targetUrl);
                case "DELETE" -> restClient.delete().uri(targetUrl);
                default -> {
                    var postSpec = request.getMethod().equals("PUT")
                            ? restClient.put().uri(targetUrl)
                            : restClient.post().uri(targetUrl);
                    if (body != null && contentType != null) {
                        yield postSpec.header("Content-Type", contentType).body(body);
                    }
                    yield postSpec;
                }
            };

            String responseBody = spec.retrieve().body(String.class);
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(503)
                    .body("{\"error\": \"Service unavailable: " + e.getMessage() + "\"}");
        }
    }
}
