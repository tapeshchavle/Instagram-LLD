package com.instagram.engagement.service;

import com.instagram.common.dto.request.CreateCommentRequest;
import com.instagram.common.dto.response.CommentResponse;

import java.util.List;

/**
 * Service interface for engagement operations.
 */
public interface EngagementService {

    void likePost(String userId, String postId);

    void unlikePost(String userId, String postId);

    CommentResponse addComment(CreateCommentRequest request);

    List<CommentResponse> getComments(String postId, int page, int size);

    void sharePost(String userId, String postId, String sharedToUserId);
}
