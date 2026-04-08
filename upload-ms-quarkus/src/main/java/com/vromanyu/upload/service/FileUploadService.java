package com.vromanyu.upload.service;

import com.vromanyu.upload.dto.*;

public interface FileUploadService {
    FileUploadResponse uploadFile(FileUploadRequest fileUploadRequest);

    FileUploadStatusResponse getFileStatus(String fileUuid);

    AllUserFilesStatusResponse getAllUserFiles(String userName);

    GetFileUrlResponse getFileUrl(String fileUuid);
}
