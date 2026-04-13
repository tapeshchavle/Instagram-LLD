package com.instagram.post.service;

/**
 * Service interface for media operations.
 * Handles pre-signed URL generation for direct client-to-storage uploads.
 */
public interface MediaService {

    /**
     * Generates a simulated pre-signed URL for direct upload to object storage.
     * In production, this would call AWS S3 or GCS to generate a real pre-signed URL.
     */
    String generatePresignedUrl(String fileName, String contentType);
}
