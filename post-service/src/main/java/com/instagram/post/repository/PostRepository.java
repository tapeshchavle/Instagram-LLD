package com.instagram.post.repository;

import com.instagram.post.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for Post entities.
 * Maintains indexes by post ID and user ID for efficient lookups.
 */
@Repository
public class PostRepository {

    private final Map<String, Post> postsById = new ConcurrentHashMap<>();
    private final Map<String, List<String>> postIdsByUserId = new ConcurrentHashMap<>();

    public Post save(Post post) {
        postsById.put(post.getId(), post);
        postIdsByUserId.computeIfAbsent(post.getUserId(), k -> Collections.synchronizedList(new ArrayList<>()));
        if (!postIdsByUserId.get(post.getUserId()).contains(post.getId())) {
            postIdsByUserId.get(post.getUserId()).add(0, post.getId()); // newest first
        }
        return post;
    }

    public Optional<Post> findById(String id) {
        return Optional.ofNullable(postsById.get(id));
    }

    public List<Post> findByUserId(String userId) {
        List<String> postIds = postIdsByUserId.getOrDefault(userId, Collections.emptyList());
        return postIds.stream()
                .map(postsById::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Post> findByUserIdPaginated(String userId, int page, int size) {
        List<String> postIds = postIdsByUserId.getOrDefault(userId, Collections.emptyList());
        int start = page * size;
        int end = Math.min(start + size, postIds.size());
        if (start >= postIds.size()) return Collections.emptyList();

        return postIds.subList(start, end).stream()
                .map(postsById::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        Post post = postsById.remove(id);
        if (post != null) {
            List<String> userPosts = postIdsByUserId.get(post.getUserId());
            if (userPosts != null) userPosts.remove(id);
        }
    }

    public boolean existsById(String id) {
        return postsById.containsKey(id);
    }

    public Collection<Post> findAll() {
        return postsById.values();
    }
}
