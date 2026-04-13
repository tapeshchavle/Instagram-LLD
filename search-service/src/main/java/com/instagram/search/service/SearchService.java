package com.instagram.search.service;

import com.instagram.common.dto.response.SearchResponse;

public interface SearchService {

    SearchResponse searchUsers(String query, int limit);

    SearchResponse searchHashtags(String query, int limit);

    SearchResponse searchPosts(String query, int limit);

    void indexPost(String postId, String userId, String caption);

    void indexUser(String userId, String username);

    void removePost(String postId);
}
