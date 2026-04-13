package com.instagram.post.service.impl;

import com.instagram.common.dto.request.CreatePostRequest;
import com.instagram.common.dto.response.PostResponse;
import com.instagram.common.enums.PostStatus;
import com.instagram.common.event.PostCreatedEvent;
import com.instagram.common.event.PostDeletedEvent;
import com.instagram.common.exception.ResourceNotFoundException;
import com.instagram.common.exception.UnauthorizedException;
import com.instagram.post.model.Hashtag;
import com.instagram.post.model.Media;
import com.instagram.post.model.Post;
import com.instagram.post.observer.PostEventPublisher;
import com.instagram.post.repository.HashtagRepository;
import com.instagram.post.repository.MediaRepository;
import com.instagram.post.repository.PostRepository;
import com.instagram.post.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Implementation of PostService.
 * Uses Builder pattern for post creation and Observer pattern for event publishing.
 */
@Service
public class PostServiceImpl implements PostService {

    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);
    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#(\\w+)");

    private final PostRepository postRepository;
    private final MediaRepository mediaRepository;
    private final HashtagRepository hashtagRepository;
    private final PostEventPublisher eventPublisher;
    private final RestClient restClient;

    @Value("${instagram.user-service.url:http://localhost:8081}")
    private String userServiceUrl;

    public PostServiceImpl(PostRepository postRepository, MediaRepository mediaRepository,
                           HashtagRepository hashtagRepository, PostEventPublisher eventPublisher) {
        this.postRepository = postRepository;
        this.mediaRepository = mediaRepository;
        this.hashtagRepository = hashtagRepository;
        this.eventPublisher = eventPublisher;
        this.restClient = RestClient.create();
    }

    @Override
    public PostResponse createPost(CreatePostRequest request) {
        String postId = UUID.randomUUID().toString();

        // Extract hashtags from caption
        List<String> hashtags = extractHashtags(request.getCaption());

        // Build media list
        List<Media> mediaList = new ArrayList<>();
        if (request.getMediaItems() != null) {
            for (int i = 0; i < request.getMediaItems().size(); i++) {
                CreatePostRequest.MediaItem item = request.getMediaItems().get(i);
                Media media = new Media(
                        UUID.randomUUID().toString(),
                        postId,
                        item.getUrl(),
                        item.getMediaType(),
                        i,
                        item.getWidth(),
                        item.getHeight()
                );
                mediaRepository.save(media);
                mediaList.add(media);
            }
        }

        // Use Builder pattern to construct the post
        Post post = new Post.Builder(postId, request.getUserId())
                .caption(request.getCaption())
                .mediaList(mediaList)
                .hashtags(hashtags)
                .status(PostStatus.PUBLISHED)
                .build();

        postRepository.save(post);

        // Index hashtags
        for (String tag : hashtags) {
            hashtagRepository.getOrCreate(tag, UUID.randomUUID().toString());
        }

        // Notify user service to increment post count
        try {
            restClient.post()
                    .uri(userServiceUrl + "/api/v1/users/" + request.getUserId() + "/increment-post-count")
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("User Service unavailable for post count increment: {}", e.getMessage());
        }

        // Publish event via Observer pattern
        PostCreatedEvent event = new PostCreatedEvent(postId, request.getUserId(), request.getCaption(), 0);
        eventPublisher.publishPostCreated(event);

        log.info("Post created: {} by user: {} with {} media items and {} hashtags",
                postId, request.getUserId(), mediaList.size(), hashtags.size());

        return toResponse(post);
    }

    @Override
    public PostResponse getPostById(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        return toResponse(post);
    }

    @Override
    public List<PostResponse> getPostsByUserId(String userId, int page, int size) {
        return postRepository.findByUserIdPaginated(userId, page, size).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePost(String postId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));

        if (!post.getUserId().equals(userId)) {
            throw new UnauthorizedException("Only the post owner can delete this post");
        }

        post.setStatus(PostStatus.DELETED);
        postRepository.deleteById(postId);

        eventPublisher.publishPostDeleted(new PostDeletedEvent(postId, userId));
        log.info("Post deleted: {} by user: {}", postId, userId);
    }

    @Override
    public void incrementLikeCount(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        post.incrementLikeCount();
    }

    @Override
    public void decrementLikeCount(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        post.decrementLikeCount();
    }

    @Override
    public void incrementCommentCount(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        post.incrementCommentCount();
    }

    @Override
    public void incrementShareCount(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        post.incrementShareCount();
    }

    /**
     * Extracts hashtags from a caption string.
     * Example: "Hello #world #travel" → ["world", "travel"]
     */
    private List<String> extractHashtags(String caption) {
        if (caption == null || caption.isEmpty()) return Collections.emptyList();
        List<String> hashtags = new ArrayList<>();
        Matcher matcher = HASHTAG_PATTERN.matcher(caption);
        while (matcher.find()) {
            hashtags.add(matcher.group(1).toLowerCase());
        }
        return hashtags;
    }

    private PostResponse toResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setPostId(post.getId());
        response.setUserId(post.getUserId());
        response.setCaption(post.getCaption());
        response.setHashtags(post.getHashtags());
        response.setStatus(post.getStatus());
        response.setLikeCount(post.getLikeCount());
        response.setCommentCount(post.getCommentCount());
        response.setShareCount(post.getShareCount());
        response.setCreatedAt(post.getCreatedAt());

        List<PostResponse.MediaItem> mediaItems = post.getMediaList().stream()
                .map(m -> {
                    PostResponse.MediaItem item = new PostResponse.MediaItem();
                    item.setMediaId(m.getId());
                    item.setUrl(m.getUrl());
                    item.setMediaType(m.getMediaType());
                    item.setOrder(m.getOrder());
                    return item;
                }).collect(Collectors.toList());
        response.setMedia(mediaItems);

        return response;
    }
}
