package com.instagram.engagement.decorator;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Chains multiple ContentFilters together in order.
 * Each filter's output becomes the next filter's input.
 *
 * Design Pattern: Decorator (Chain of Responsibility variant)
 */
@Component
public class ContentFilterChain {

    private final List<ContentFilter> filters;

    public ContentFilterChain(List<ContentFilter> filters) {
        this.filters = filters;
    }

    /**
     * Applies all registered filters in order.
     *
     * @param content Raw content
     * @return Filtered content after all filters applied
     */
    public String apply(String content) {
        String result = content;
        for (ContentFilter filter : filters) {
            result = filter.filter(result);
        }
        return result;
    }
}
