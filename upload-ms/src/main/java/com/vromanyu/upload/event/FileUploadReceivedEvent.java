package com.vromanyu.upload.event;

import java.time.OffsetDateTime;

public record FileUploadReceivedEvent(String fileUuid,
                                      String fileName,
                                      byte[] fileData,
                                      String userName,
                                      OffsetDateTime uploadedAt) {
}
