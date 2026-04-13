package com.instagram.post.repository;

import com.instagram.post.model.Hashtag;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for Hashtag entities.
 * Maintains index by tag name for deduplication and search.
 */
@Repository
public class HashtagRepository {

    private final Map<String, Hashtag> hashtagsById = new ConcurrentHashMap<>();
    private final Map<String, Hashtag> hashtagsByTag = new ConcurrentHashMap<>();

    public Hashtag save(Hashtag hashtag) {
        hashtagsById.put(hashtag.getId(), hashtag);
        hashtagsByTag.put(hashtag.getTag().toLowerCase(), hashtag);
        return hashtag;
    }

    public Optional<Hashtag> findByTag(String tag) {
        return Optional.ofNullable(hashtagsByTag.get(tag.toLowerCase()));
    }

    public Optional<Hashtag> findById(String id) {
        return Optional.ofNullable(hashtagsById.get(id));
    }

    /**
     * Get or create a hashtag. If it already exists, increment the post count.
     */
    public Hashtag getOrCreate(String tag, String id) {
        String normalizedTag = tag.toLowerCase().replaceAll("[^a-z0-9]", "");
        Hashtag existing = hashtagsByTag.get(normalizedTag);
        if (existing != null) {
            existing.incrementPostCount();
            return existing;
        }
        Hashtag newHashtag = new Hashtag(id, normalizedTag);
        return save(newHashtag);
    }

    /**
     * Search hashtags by prefix.
     */
    public List<Hashtag> searchByPrefix(String prefix) {
        String lowerPrefix = prefix.toLowerCase();
        return hashtagsByTag.entrySet().stream()
                .filter(e -> e.getKey().startsWith(lowerPrefix))
                .map(Map.Entry::getValue)
                .sorted((a, b) -> Long.compare(b.getPostCount(), a.getPostCount()))
                .toList();
    }

    public Collection<Hashtag> findAll() {
        return hashtagsById.values();
    }
}
