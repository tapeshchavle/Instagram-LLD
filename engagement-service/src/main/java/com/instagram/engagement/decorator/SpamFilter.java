package com.instagram.engagement.decorator;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Decorator that filters spam content (excessive links, repetition, etc.).
 */
@Component
@Order(2)
public class SpamFilter implements ContentFilter {

    private static final Pattern EXCESSIVE_LINKS = Pattern.compile("(https?://\\S+\\s*){4,}");
    private static final Pattern REPEATED_CHARS = Pattern.compile("(.)\\1{9,}");
    private static final int MAX_LENGTH = 2200;

    @Override
    public String filter(String content) {
        if (content == null) return null;

        // Block excessive links
        if (EXCESSIVE_LINKS.matcher(content).find()) {
            throw new IllegalArgumentException("Comment contains too many links (spam detected)");
        }

        // Remove excessive character repetition
        String filtered = REPEATED_CHARS.matcher(content).replaceAll("$1$1$1");

        // Enforce max length
        if (filtered.length() > MAX_LENGTH) {
            filtered = filtered.substring(0, MAX_LENGTH);
        }

        return filtered;
    }
}
