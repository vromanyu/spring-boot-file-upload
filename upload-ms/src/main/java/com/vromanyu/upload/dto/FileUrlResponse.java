package com.vromanyu.upload.dto;

import java.time.Instant;

public record FileUrlResponse(String fileUuid,
                              String url,
                              String fileName,
                              String userName,
                              UploadStatus status,
                              Instant uploadedAt,
                              Instant expiresAt) {
}
