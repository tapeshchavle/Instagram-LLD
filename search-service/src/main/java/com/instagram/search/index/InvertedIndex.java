package com.instagram.search.index;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Inverted index for full-text search of post captions.
 * Maps tokens (words) to the set of post IDs containing them.
 *
 * In production, this would be Elasticsearch with custom analyzers.
 */
@Component
public class InvertedIndex {

    // token -> Set<postId>
    private final Map<String, Set<String>> index = new ConcurrentHashMap<>();

    // postId -> Set<tokens> (for efficient removal)
    private final Map<String, Set<String>> reverseIndex = new ConcurrentHashMap<>();

    /**
     * Indexes a document (post caption) by tokenizing and storing in the inverted index.
     */
    public void indexDocument(String postId, String content) {
        if (content == null || content.isEmpty()) return;

        Set<String> tokens = tokenize(content);
        reverseIndex.put(postId, tokens);

        for (String token : tokens) {
            index.computeIfAbsent(token, k -> ConcurrentHashMap.newKeySet()).add(postId);
        }
    }

    /**
     * Searches for posts containing ALL query tokens (AND semantics).
     */
    public Set<String> search(String query) {
        Set<String> queryTokens = tokenize(query);
        if (queryTokens.isEmpty()) return Collections.emptySet();

        Iterator<String> it = queryTokens.iterator();
        Set<String> result = new HashSet<>(index.getOrDefault(it.next(), Collections.emptySet()));

        while (it.hasNext()) {
            Set<String> postIds = index.getOrDefault(it.next(), Collections.emptySet());
            result.retainAll(postIds); // Intersection for AND semantics
        }

        return result;
    }

    /**
     * Removes a document from the index.
     */
    public void removeDocument(String postId) {
        Set<String> tokens = reverseIndex.remove(postId);
        if (tokens != null) {
            for (String token : tokens) {
                Set<String> postIds = index.get(token);
                if (postIds != null) {
                    postIds.remove(postId);
                    if (postIds.isEmpty()) index.remove(token);
                }
            }
        }
    }

    /**
     * Tokenizes content into lowercase words, filtering short tokens.
     */
    private Set<String> tokenize(String content) {
        Set<String> tokens = new HashSet<>();
        String[] words = content.toLowerCase().replaceAll("[^a-z0-9\\s]", "").split("\\s+");
        for (String word : words) {
            if (word.length() >= 2) {
                tokens.add(word);
            }
        }
        return tokens;
    }
}
