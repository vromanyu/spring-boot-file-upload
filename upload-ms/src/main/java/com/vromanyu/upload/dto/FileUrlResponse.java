package com.vromanyu.upload.dto;

import java.time.OffsetDateTime;

public record FileUrlResponse(String fileUuid,
                              String url,
                              String fileName,
                              String userName,
                              UploadStatus status,
                              OffsetDateTime uploadedAt,
                              OffsetDateTime expiresAt) {
}
