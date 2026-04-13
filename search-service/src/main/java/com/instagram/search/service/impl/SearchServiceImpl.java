package com.instagram.search.service.impl;

import com.instagram.common.dto.response.SearchResponse;
import com.instagram.common.dto.response.UserProfileResponse;
import com.instagram.search.index.SearchIndexManager;
import com.instagram.search.index.TrieIndex;
import com.instagram.search.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);
    private final SearchIndexManager indexManager;

    public SearchServiceImpl(SearchIndexManager indexManager) {
        this.indexManager = indexManager;
    }

    @Override
    public SearchResponse searchUsers(String query, int limit) {
        log.info("Searching users with query: {}", query);
        List<TrieIndex.IndexEntry> results = indexManager.getUserIndex().searchByPrefix(query, limit);

        SearchResponse response = new SearchResponse();
        response.setQuery(query);
        response.setType("users");
        response.setUsers(results.stream().map(entry -> {
            UserProfileResponse user = new UserProfileResponse();
            user.setUserId(entry.id());
            user.setUsername(entry.originalTerm());
            return user;
        }).collect(Collectors.toList()));
        response.setTotalResults(results.size());
        return response;
    }

    @Override
    public SearchResponse searchHashtags(String query, int limit) {
        log.info("Searching hashtags with query: {}", query);
        List<TrieIndex.IndexEntry> results = indexManager.getHashtagIndex().searchByPrefix(query, limit);

        SearchResponse response = new SearchResponse();
        response.setQuery(query);
        response.setType("hashtags");
        response.setHashtags(results.stream()
                .map(entry -> new SearchResponse.HashtagResult(entry.originalTerm(), (long) entry.score()))
                .collect(Collectors.toList()));
        response.setTotalResults(results.size());
        return response;
    }

    @Override
    public SearchResponse searchPosts(String query, int limit) {
        log.info("Searching posts with query: {}", query);
        Set<String> postIds = indexManager.getPostIndex().search(query);

        SearchResponse response = new SearchResponse();
        response.setQuery(query);
        response.setType("posts");
        response.setTotalResults(postIds.size());
        return response;
    }

    @Override
    public void indexPost(String postId, String userId, String caption) {
        indexManager.indexPost(postId, userId, caption);
        log.info("Indexed post: {} by user: {}", postId, userId);
    }

    @Override
    public void indexUser(String userId, String username) {
        indexManager.indexUser(userId, username, 1.0);
        log.info("Indexed user: {} ({})", username, userId);
    }

    @Override
    public void removePost(String postId) {
        indexManager.removePost(postId);
        log.info("Removed post from index: {}", postId);
    }
}
