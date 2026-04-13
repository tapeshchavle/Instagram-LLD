package com.instagram.post.service.impl;

import com.instagram.post.service.MediaService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of MediaService.
 * Simulates pre-signed URL generation for direct client-to-storage uploads.
 * In production, this would integrate with AWS S3's generatePresignedUrl().
 */
@Service
public class MediaServiceImpl implements MediaService {

    private static final String SIMULATED_CDN_BASE = "https://cdn.instagram-lld.com/media/";

    @Override
    public String generatePresignedUrl(String fileName, String contentType) {
        String key = UUID.randomUUID().toString() + "/" + fileName;
        return SIMULATED_CDN_BASE + key + "?presigned=true&expires=3600";
    }
}
