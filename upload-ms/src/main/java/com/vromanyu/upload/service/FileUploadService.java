package com.vromanyu.upload.service;


import com.vromanyu.upload.dto.FileUploadRequest;
import com.vromanyu.upload.dto.FileUploadResponse;

public interface FileUploadService {
    FileUploadResponse uploadFile(FileUploadRequest fileUploadRequest);
}
