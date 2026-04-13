package com.instagram.engagement.repository;

import com.instagram.engagement.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CommentRepository {

    private final Map<String, Comment> commentsById = new ConcurrentHashMap<>();
    private final Map<String, List<Comment>> commentsByPostId = new ConcurrentHashMap<>();

    public Comment save(Comment comment) {
        commentsById.put(comment.getId(), comment);
        commentsByPostId.computeIfAbsent(comment.getPostId(),
                k -> Collections.synchronizedList(new ArrayList<>())).add(comment);
        return comment;
    }

    public Optional<Comment> findById(String id) {
        return Optional.ofNullable(commentsById.get(id));
    }

    public List<Comment> findByPostId(String postId) {
        return commentsByPostId.getOrDefault(postId, Collections.emptyList());
    }

    public List<Comment> findByPostIdPaginated(String postId, int page, int size) {
        List<Comment> comments = commentsByPostId.getOrDefault(postId, Collections.emptyList());
        int start = page * size;
        int end = Math.min(start + size, comments.size());
        if (start >= comments.size()) return Collections.emptyList();
        return new ArrayList<>(comments.subList(start, end));
    }

    public long countByPostId(String postId) {
        List<Comment> comments = commentsByPostId.get(postId);
        return comments != null ? comments.size() : 0;
    }
}
