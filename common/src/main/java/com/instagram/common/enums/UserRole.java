package com.instagram.common.enums;

/**
 * Represents the type/role of a user account.
 * Users with follower count exceeding the celebrity threshold
 * are automatically promoted to CELEBRITY for feed strategy optimization.
 */
public enum UserRole {
    NORMAL,
    CELEBRITY,
    VERIFIED,
    BUSINESS
}
