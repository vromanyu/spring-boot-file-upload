package com.vromanyu.upload.event;

import com.vromanyu.upload.annotations.FileUploadEvent;

import java.time.Instant;

@FileUploadEvent
public record FileUploadEventData(String fileUuid,
                                  String fileName,
                                  byte[] fileData,
                                  String userName,
                                  Instant uploadedAt) {
}
