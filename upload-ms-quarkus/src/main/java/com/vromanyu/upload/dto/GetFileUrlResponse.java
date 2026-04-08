package com.vromanyu.upload.dto;

import com.vromanyu.upload.enums.UploadStatus;

import java.time.Instant;

public record GetFileUrlResponse(String fileUuid,
                                 String url,
                                 String fileName,
                                 String userName,
                                 UploadStatus status,
                                 Instant uploadedAt,
                                 Instant expiresAt) {
}

