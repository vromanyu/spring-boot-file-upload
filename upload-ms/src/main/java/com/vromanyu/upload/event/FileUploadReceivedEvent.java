package com.vromanyu.upload.event;

import java.time.Instant;

public record FileUploadReceivedEvent(String fileUuid,
                                      String fileName,
                                      byte[] fileData,
                                      String userName,
                                      Instant uploadedAt) {
}
