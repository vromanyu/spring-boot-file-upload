package com.vromanyu.service;

import com.vromanyu.dto.AllUserFilesResponse;
import com.vromanyu.dto.FileUploadRequest;
import com.vromanyu.dto.FileUploadResponse;
import com.vromanyu.dto.FileUploadStatusResponse;

public interface FileUploadService {
    FileUploadResponse uploadFile(FileUploadRequest fileUploadRequest);

    FileUploadStatusResponse getFileStatus(String fileUuid);

    AllUserFilesResponse getAllUserFiles(String userName);
}
