package com.instagram.engagement.repository;

import com.instagram.engagement.model.Share;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ShareRepository {

    private final Map<String, Share> sharesById = new ConcurrentHashMap<>();
    private final Map<String, List<Share>> sharesByPostId = new ConcurrentHashMap<>();

    public Share save(Share share) {
        sharesById.put(share.getId(), share);
        sharesByPostId.computeIfAbsent(share.getPostId(),
                k -> Collections.synchronizedList(new ArrayList<>())).add(share);
        return share;
    }

    public long countByPostId(String postId) {
        List<Share> shares = sharesByPostId.get(postId);
        return shares != null ? shares.size() : 0;
    }
}
