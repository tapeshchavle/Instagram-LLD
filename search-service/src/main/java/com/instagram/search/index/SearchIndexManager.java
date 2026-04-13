package com.instagram.search.index;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Manages all search indexes (Trie for users/hashtags, Inverted for posts).
 * Acts as a Singleton coordinator for index operations.
 */
@Component
public class SearchIndexManager {

    private final TrieIndex userIndex;
    private final TrieIndex hashtagIndex;
    private final InvertedIndex postIndex;

    // Store post metadata for retrieval
    private final Map<String, PostIndexEntry> postMetadata = new ConcurrentHashMap<>();

    public SearchIndexManager(TrieIndex userIndex, InvertedIndex postIndex) {
        this.userIndex = userIndex;
        this.hashtagIndex = new TrieIndex(); // Separate trie for hashtags
        this.postIndex = postIndex;
    }

    public void indexUser(String userId, String username, double score) {
        userIndex.insert(username, userId, score);
    }

    public void indexPost(String postId, String userId, String caption) {
        postIndex.indexDocument(postId, caption);
        postMetadata.put(postId, new PostIndexEntry(postId, userId, caption));

        // Also index hashtags from caption
        if (caption != null) {
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("#(\\w+)").matcher(caption);
            while (matcher.find()) {
                String tag = matcher.group(1).toLowerCase();
                hashtagIndex.insert(tag, tag, 1.0);
            }
        }
    }

    public void removePost(String postId) {
        postIndex.removeDocument(postId);
        postMetadata.remove(postId);
    }

    public TrieIndex getUserIndex() { return userIndex; }
    public TrieIndex getHashtagIndex() { return hashtagIndex; }
    public InvertedIndex getPostIndex() { return postIndex; }
    public Map<String, PostIndexEntry> getPostMetadata() { return postMetadata; }

    public record PostIndexEntry(String postId, String userId, String caption) {}
}
