package com.instagram.engagement.decorator;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Decorator that filters profane content from comments and captions.
 * Replaces offensive words with asterisks.
 */
@Component
@Order(1)
public class ProfanityFilter implements ContentFilter {

    private static final Set<String> BLOCKED_WORDS = Set.of(
            "spam", "abuse", "hate", "violence"
    );

    @Override
    public String filter(String content) {
        if (content == null) return null;

        String filtered = content;
        for (String word : BLOCKED_WORDS) {
            filtered = filtered.replaceAll("(?i)\\b" + word + "\\b", "*".repeat(word.length()));
        }
        return filtered;
    }
}
