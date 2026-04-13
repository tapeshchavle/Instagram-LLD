package com.instagram.user.repository;

import com.instagram.user.model.FollowRelation;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for follow relationships.
 * Maintains bidirectional indexes for fast follower/following lookups.
 * In production, this would use a graph database (Neo4j) or Cassandra.
 */
@Repository
public class FollowRepository {

    private final Map<String, FollowRelation> relationsById = new ConcurrentHashMap<>();

    // followerId -> Set of followeeIds
    private final Map<String, Set<String>> followingIndex = new ConcurrentHashMap<>();

    // followeeId -> Set of followerIds
    private final Map<String, Set<String>> followersIndex = new ConcurrentHashMap<>();

    // Composite key "followerId:followeeId" -> relation
    private final Map<String, FollowRelation> relationByPair = new ConcurrentHashMap<>();

    public FollowRelation save(FollowRelation relation) {
        relationsById.put(relation.getId(), relation);

        String compositeKey = compositeKey(relation.getFollowerId(), relation.getFolloweeId());
        relationByPair.put(compositeKey, relation);

        followingIndex.computeIfAbsent(relation.getFollowerId(), k -> ConcurrentHashMap.newKeySet())
                .add(relation.getFolloweeId());
        followersIndex.computeIfAbsent(relation.getFolloweeId(), k -> ConcurrentHashMap.newKeySet())
                .add(relation.getFollowerId());

        return relation;
    }

    public void delete(String followerId, String followeeId) {
        String compositeKey = compositeKey(followerId, followeeId);
        FollowRelation relation = relationByPair.remove(compositeKey);
        if (relation != null) {
            relationsById.remove(relation.getId());
            Set<String> following = followingIndex.get(followerId);
            if (following != null) following.remove(followeeId);
            Set<String> followers = followersIndex.get(followeeId);
            if (followers != null) followers.remove(followerId);
        }
    }

    public boolean exists(String followerId, String followeeId) {
        return relationByPair.containsKey(compositeKey(followerId, followeeId));
    }

    public Optional<FollowRelation> findByPair(String followerId, String followeeId) {
        return Optional.ofNullable(relationByPair.get(compositeKey(followerId, followeeId)));
    }

    /**
     * Returns the set of user IDs that the given user follows.
     */
    public Set<String> getFollowing(String userId) {
        return followingIndex.getOrDefault(userId, Collections.emptySet());
    }

    /**
     * Returns the set of user IDs that follow the given user.
     */
    public Set<String> getFollowers(String userId) {
        return followersIndex.getOrDefault(userId, Collections.emptySet());
    }

    public long getFollowerCount(String userId) {
        return getFollowers(userId).size();
    }

    public long getFollowingCount(String userId) {
        return getFollowing(userId).size();
    }

    private String compositeKey(String followerId, String followeeId) {
        return followerId + ":" + followeeId;
    }
}
