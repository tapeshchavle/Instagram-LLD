package com.instagram.engagement.repository;

import com.instagram.engagement.model.Like;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LikeRepository {

    private final Map<String, Like> likesById = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> likesByPostId = new ConcurrentHashMap<>(); // postId -> Set<userId>
    private final Map<String, Like> likeByUserPost = new ConcurrentHashMap<>(); // "userId:postId" -> Like

    public Like save(Like like) {
        likesById.put(like.getId(), like);
        likesByPostId.computeIfAbsent(like.getPostId(), k -> ConcurrentHashMap.newKeySet())
                .add(like.getUserId());
        likeByUserPost.put(compositeKey(like.getUserId(), like.getPostId()), like);
        return like;
    }

    public void delete(String userId, String postId) {
        Like like = likeByUserPost.remove(compositeKey(userId, postId));
        if (like != null) {
            likesById.remove(like.getId());
            Set<String> postLikes = likesByPostId.get(postId);
            if (postLikes != null) postLikes.remove(userId);
        }
    }

    public boolean exists(String userId, String postId) {
        return likeByUserPost.containsKey(compositeKey(userId, postId));
    }

    public long countByPostId(String postId) {
        Set<String> likes = likesByPostId.get(postId);
        return likes != null ? likes.size() : 0;
    }

    private String compositeKey(String userId, String postId) {
        return userId + ":" + postId;
    }
}
