package com.instagram.user.repository;

import com.instagram.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for User entities.
 * Uses ConcurrentHashMap for thread-safe operations.
 * In production, this would be backed by PostgreSQL.
 */
@Repository
public class UserRepository {

    private final Map<String, User> usersById = new ConcurrentHashMap<>();
    private final Map<String, User> usersByUsername = new ConcurrentHashMap<>();
    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();

    public User save(User user) {
        usersById.put(user.getId(), user);
        usersByUsername.put(user.getUsername().toLowerCase(), user);
        usersByEmail.put(user.getEmail().toLowerCase(), user);
        return user;
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(usersById.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username.toLowerCase()));
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(usersByEmail.get(email.toLowerCase()));
    }

    public boolean existsByUsername(String username) {
        return usersByUsername.containsKey(username.toLowerCase());
    }

    public boolean existsByEmail(String email) {
        return usersByEmail.containsKey(email.toLowerCase());
    }

    public List<User> findAll() {
        return new ArrayList<>(usersById.values());
    }

    public void deleteById(String id) {
        User user = usersById.remove(id);
        if (user != null) {
            usersByUsername.remove(user.getUsername().toLowerCase());
            usersByEmail.remove(user.getEmail().toLowerCase());
        }
    }

    /**
     * Search users by username prefix (for autocomplete).
     */
    public List<User> searchByUsernamePrefix(String prefix) {
        String lowerPrefix = prefix.toLowerCase();
        return usersByUsername.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(lowerPrefix))
                .map(Map.Entry::getValue)
                .toList();
    }
}
