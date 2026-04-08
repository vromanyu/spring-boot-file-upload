package com.vromanyu.upload.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vromanyu.upload.enums.UploadStatus;

import java.time.Instant;

public record FileUploadResponse(@JsonProperty("file_uuid") String fileUuid,
                                 @JsonProperty("file_name") String fileName,
                                 @JsonProperty("user_name") String userName,
                                 @JsonProperty("status") UploadStatus status,
                                 @JsonProperty("uploaded_at") Instant uploadedAt) {
}
