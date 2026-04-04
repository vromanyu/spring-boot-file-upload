package com.vromanyu.upload.service;

import com.vromanyu.upload.event.FileUploadReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FileUploadProcessorImpl implements FileUploadProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadProcessorImpl.class);


    @Override
    @Transactional
    @Async
    @EventListener
    public void processFileUpload(FileUploadReceivedEvent fileUploadReceivedEvent) {
        logger.info("received file upload event: {}", fileUploadReceivedEvent);
    }
}
