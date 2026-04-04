package com.vromanyu.upload.service;

import com.vromanyu.upload.event.FileUploadReceivedEvent;

public interface FileUploadProcessor {
    void processFileUpload(FileUploadReceivedEvent fileUploadReceivedEvent);
}
