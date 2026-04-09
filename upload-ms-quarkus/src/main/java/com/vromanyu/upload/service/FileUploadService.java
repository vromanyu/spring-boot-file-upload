package com.vromanyu.upload.service;

import com.vromanyu.upload.dto.*;

public interface FileUploadService {
    FileUploadResponse uploadFile(FileUploadRequest fileUploadRequest, String userName);

    FileUploadStatusResponse getFileStatus(String fileUuid, String userName);

    AllUserFilesStatusResponse getAllUserFiles(String userName);

    GetFileUrlResponse getFileUrl(String fileUuid, String userName);
}
