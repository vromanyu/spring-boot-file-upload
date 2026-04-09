package com.vromanyu.upload.service;

import com.vromanyu.upload.event.FileUploadEventData;

public interface FileUploadProcessorService {
    void processUploadedFile(FileUploadEventData fileUploadEventData);
}
