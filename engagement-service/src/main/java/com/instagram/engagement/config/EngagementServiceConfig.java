package com.instagram.engagement.config;

import org.springframework.stereotype.Component;

@Component
public class EngagementServiceConfig {

    private int maxCommentLength = 2200;
    private int maxCommentsPerPage = 50;

    public int getMaxCommentLength() { return maxCommentLength; }
    public void setMaxCommentLength(int maxCommentLength) { this.maxCommentLength = maxCommentLength; }
    public int getMaxCommentsPerPage() { return maxCommentsPerPage; }
    public void setMaxCommentsPerPage(int maxCommentsPerPage) { this.maxCommentsPerPage = maxCommentsPerPage; }
}
