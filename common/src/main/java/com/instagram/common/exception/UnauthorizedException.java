package com.instagram.common.exception;

/**
 * Thrown when a user attempts an action they are not authorized to perform.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
