package com.vromanyu.upload.service;

import com.vromanyu.upload.annotations.FileUploadEvent;
import com.vromanyu.upload.event.FileUploadEventData;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class FileUploadProcessorServiceImpl implements FileUploadProcessorService {

    @Override
    public void processUploadedFile(@ObservesAsync @FileUploadEvent FileUploadEventData fileUploadEventData) {
        Log.infof("processing uploaded file: %s", fileUploadEventData);
    }
}
