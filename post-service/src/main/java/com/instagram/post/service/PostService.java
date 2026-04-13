package com.instagram.post.service;

import com.instagram.common.dto.request.CreatePostRequest;
import com.instagram.common.dto.response.PostResponse;
import java.util.List;

/**
 * Service interface for post management.
 */
public interface PostService {

    PostResponse createPost(CreatePostRequest request);

    PostResponse getPostById(String postId);

    List<PostResponse> getPostsByUserId(String userId, int page, int size);

    void deletePost(String postId, String userId);

    void incrementLikeCount(String postId);

    void decrementLikeCount(String postId);

    void incrementCommentCount(String postId);

    void incrementShareCount(String postId);
}
