package com.vromanyu.service;

import com.vromanyu.dto.FileUploadRequest;
import com.vromanyu.dto.FileUploadResponse;

public interface FileUploadService {
    FileUploadResponse uploadFile(FileUploadRequest fileUploadRequest);
}
