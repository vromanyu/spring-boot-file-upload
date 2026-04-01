package com.vromanyu.upload.service;

import com.vromanyu.upload.dto.AllUserFilesResponse;
import com.vromanyu.upload.dto.FileUploadStatusResponse;

public interface FileUploadStatusService {
    FileUploadStatusResponse getFileStatus(String fileUuid);

    AllUserFilesResponse getAllUserFiles(String userName);
}
