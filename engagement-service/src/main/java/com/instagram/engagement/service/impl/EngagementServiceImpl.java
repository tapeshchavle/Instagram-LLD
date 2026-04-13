package com.instagram.engagement.service.impl;

import com.instagram.common.dto.request.CreateCommentRequest;
import com.instagram.common.dto.response.CommentResponse;
import com.instagram.common.exception.DuplicateResourceException;
import com.instagram.engagement.decorator.ContentFilterChain;
import com.instagram.engagement.factory.EngagementFactory;
import com.instagram.engagement.model.Comment;
import com.instagram.engagement.model.Like;
import com.instagram.engagement.model.Share;
import com.instagram.engagement.repository.CommentRepository;
import com.instagram.engagement.repository.LikeRepository;
import com.instagram.engagement.repository.ShareRepository;
import com.instagram.engagement.service.EngagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of EngagementService.
 * Uses Factory pattern for engagement creation and Decorator chain for content filtering.
 */
@Service
public class EngagementServiceImpl implements EngagementService {

    private static final Logger log = LoggerFactory.getLogger(EngagementServiceImpl.class);

    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final ShareRepository shareRepository;
    private final EngagementFactory engagementFactory;
    private final ContentFilterChain contentFilterChain;
    private final RestClient restClient;

    @Value("${instagram.post-service.url:http://localhost:8082}")
    private String postServiceUrl;

    @Value("${instagram.notification-service.url:http://localhost:8086}")
    private String notificationServiceUrl;

    public EngagementServiceImpl(LikeRepository likeRepository,
                                  CommentRepository commentRepository,
                                  ShareRepository shareRepository,
                                  EngagementFactory engagementFactory,
                                  ContentFilterChain contentFilterChain) {
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.shareRepository = shareRepository;
        this.engagementFactory = engagementFactory;
        this.contentFilterChain = contentFilterChain;
        this.restClient = RestClient.create();
    }

    @Override
    public void likePost(String userId, String postId) {
        if (likeRepository.exists(userId, postId)) {
            throw new DuplicateResourceException("Like", userId + " -> " + postId);
        }

        // Use Factory pattern to create the Like
        Like like = engagementFactory.createLike(userId, postId);
        likeRepository.save(like);

        // Notify Post Service to increment like count
        notifyPostService(postId, "/increment-like");

        log.info("User {} liked post {}", userId, postId);
    }

    @Override
    public void unlikePost(String userId, String postId) {
        if (!likeRepository.exists(userId, postId)) {
            return; // Idempotent unlike
        }

        likeRepository.delete(userId, postId);
        notifyPostService(postId, "/decrement-like");

        log.info("User {} unliked post {}", userId, postId);
    }

    @Override
    public CommentResponse addComment(CreateCommentRequest request) {
        // Apply Decorator chain: profanity filter → spam filter
        String filteredContent = contentFilterChain.apply(request.getContent());

        // Use Factory pattern to create the Comment
        Comment comment = engagementFactory.createComment(
                request.getUserId(),
                request.getPostId(),
                filteredContent,
                request.getParentCommentId()
        );

        commentRepository.save(comment);
        notifyPostService(request.getPostId(), "/increment-comment");

        log.info("User {} commented on post {}", request.getUserId(), request.getPostId());

        return toCommentResponse(comment);
    }

    @Override
    public List<CommentResponse> getComments(String postId, int page, int size) {
        return commentRepository.findByPostIdPaginated(postId, page, size).stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void sharePost(String userId, String postId, String sharedToUserId) {
        Share share = engagementFactory.createShare(userId, postId, sharedToUserId);
        shareRepository.save(share);
        notifyPostService(postId, "/increment-share");

        log.info("User {} shared post {} to user {}", userId, postId, sharedToUserId);
    }

    private void notifyPostService(String postId, String action) {
        try {
            restClient.post()
                    .uri(postServiceUrl + "/api/v1/posts/" + postId + action)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Failed to notify Post Service for post {} action {}: {}",
                    postId, action, e.getMessage());
        }
    }

    private CommentResponse toCommentResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setCommentId(comment.getId());
        response.setPostId(comment.getPostId());
        response.setUserId(comment.getUserId());
        response.setContent(comment.getContent());
        response.setParentCommentId(comment.getParentCommentId());
        response.setCreatedAt(comment.getCreatedAt());
        return response;
    }
}
