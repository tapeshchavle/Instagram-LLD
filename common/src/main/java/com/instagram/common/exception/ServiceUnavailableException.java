package com.instagram.common.exception;

/**
 * Thrown when a downstream service is unavailable.
 */
public class ServiceUnavailableException extends RuntimeException {

    private final String serviceName;

    public ServiceUnavailableException(String serviceName) {
        super(String.format("Service unavailable: %s", serviceName));
        this.serviceName = serviceName;
    }

    public String getServiceName() { return serviceName; }
}
