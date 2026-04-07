package com.vromanyu.upload.utility;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.vromanyu.upload.aggregate.UserFile;

import java.util.concurrent.TimeUnit;

public class BlobUtilities {
    private static final long DURATION = 1;
    private static final TimeUnit TIME_UNIT = TimeUnit.DAYS;
    private static final String BUCKET_NAME = "spring-boot-file-upload-bucket";

    public static String uploadBlobAndGetUrl(Storage storage, UserFile userFile) {
        String blobName = userFile.getUserName() + "/" + userFile.getFileUuid() + "." + userFile.getFileContentType().substring(userFile.getFileContentType().indexOf("/") + 1);
        BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, blobName).setContentType(userFile.getFileContentType()).build();
        Blob blob = storage.create(blobInfo, userFile.getFileData());
        return blob.signUrl(DURATION, TIME_UNIT).toString();
    }
}
