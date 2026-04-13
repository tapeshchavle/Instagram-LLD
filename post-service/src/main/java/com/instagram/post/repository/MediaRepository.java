package com.instagram.post.repository;

import com.instagram.post.model.Media;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for Media entities.
 */
@Repository
public class MediaRepository {

    private final Map<String, Media> mediaById = new ConcurrentHashMap<>();
    private final Map<String, List<Media>> mediaByPostId = new ConcurrentHashMap<>();

    public Media save(Media media) {
        mediaById.put(media.getId(), media);
        mediaByPostId.computeIfAbsent(media.getPostId(), k -> Collections.synchronizedList(new ArrayList<>()))
                .add(media);
        return media;
    }

    public List<Media> findByPostId(String postId) {
        return mediaByPostId.getOrDefault(postId, Collections.emptyList());
    }

    public Optional<Media> findById(String id) {
        return Optional.ofNullable(mediaById.get(id));
    }
}
