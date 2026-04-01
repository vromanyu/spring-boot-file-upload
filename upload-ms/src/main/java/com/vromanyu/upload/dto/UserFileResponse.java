package com.vromanyu.upload.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record UserFileResponse(@JsonProperty("file_uuid") String fileUuid,
                               @JsonProperty("file_name") String fileName,
                               @JsonProperty("status") UploadStatus status,
                               @JsonProperty("uploaded_at") OffsetDateTime uploadedAt) {
}
