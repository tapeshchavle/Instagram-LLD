package com.instagram.post.config;

import org.springframework.stereotype.Component;

/**
 * Configuration for Post Service.
 */
@Component
public class PostServiceConfig {

    private int maxMediaPerPost = 10;
    private int maxCaptionLength = 2200;
    private int maxHashtagsPerPost = 30;

    public int getMaxMediaPerPost() { return maxMediaPerPost; }
    public void setMaxMediaPerPost(int maxMediaPerPost) { this.maxMediaPerPost = maxMediaPerPost; }
    public int getMaxCaptionLength() { return maxCaptionLength; }
    public void setMaxCaptionLength(int maxCaptionLength) { this.maxCaptionLength = maxCaptionLength; }
    public int getMaxHashtagsPerPost() { return maxHashtagsPerPost; }
    public void setMaxHashtagsPerPost(int maxHashtagsPerPost) { this.maxHashtagsPerPost = maxHashtagsPerPost; }
}
