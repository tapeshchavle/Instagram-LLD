package com.instagram.search.index;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Trie-based index for autocomplete / prefix search.
 * Used for username and hashtag prefix matching.
 *
 * In production, this would be Elasticsearch with edge-n-gram tokenizer.
 */
@Component
public class TrieIndex {

    private final TrieNode root = new TrieNode();

    /**
     * Inserts a term into the trie with an associated ID and score.
     */
    public void insert(String term, String id, double score) {
        String normalized = term.toLowerCase();
        TrieNode current = root;
        for (char c : normalized.toCharArray()) {
            current = current.children.computeIfAbsent(c, k -> new TrieNode());
        }
        current.isEnd = true;
        current.entries.add(new IndexEntry(id, term, score));
    }

    /**
     * Searches for all entries matching the given prefix.
     * Returns results sorted by score (descending).
     */
    public List<IndexEntry> searchByPrefix(String prefix, int limit) {
        String normalized = prefix.toLowerCase();
        TrieNode current = root;

        for (char c : normalized.toCharArray()) {
            current = current.children.get(c);
            if (current == null) return Collections.emptyList();
        }

        List<IndexEntry> results = new ArrayList<>();
        collectEntries(current, results);

        results.sort((a, b) -> Double.compare(b.score(), a.score()));
        return results.subList(0, Math.min(limit, results.size()));
    }

    private void collectEntries(TrieNode node, List<IndexEntry> results) {
        if (node.isEnd) {
            results.addAll(node.entries);
        }
        for (TrieNode child : node.children.values()) {
            collectEntries(child, results);
        }
    }

    /**
     * Removes an entry by ID.
     */
    public void remove(String term, String id) {
        String normalized = term.toLowerCase();
        TrieNode current = root;
        for (char c : normalized.toCharArray()) {
            current = current.children.get(c);
            if (current == null) return;
        }
        current.entries.removeIf(e -> e.id().equals(id));
    }

    // --- Internal data structures ---

    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEnd = false;
        List<IndexEntry> entries = new ArrayList<>();
    }

    public record IndexEntry(String id, String originalTerm, double score) {}
}
