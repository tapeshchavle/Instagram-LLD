package com.instagram.engagement.decorator;

/**
 * Base interface for content filtering.
 *
 * Design Pattern: Decorator
 * - Each filter wraps another, forming a chain
 * - New filters can be added without modifying existing ones
 * - Applied to comments and captions before persistence
 */
public interface ContentFilter {

    /**
     * Filters the input content and returns the filtered result.
     *
     * @param content The raw content to filter
     * @return Filtered content
     * @throws IllegalArgumentException if content violates filter rules
     */
    String filter(String content);
}
