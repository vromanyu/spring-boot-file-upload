package com.vromanyu.upload.service;


import com.vromanyu.upload.dto.AllUserFilesResponse;
import com.vromanyu.upload.dto.FileUploadRequest;
import com.vromanyu.upload.dto.FileUploadResponse;
import com.vromanyu.upload.dto.FileUploadStatusResponse;

public interface FileUploadService {
    FileUploadResponse uploadFile(FileUploadRequest fileUploadRequest);

    FileUploadStatusResponse getFileStatus(String fileUuid);

    AllUserFilesResponse getAllUserFiles(String userName);
}
