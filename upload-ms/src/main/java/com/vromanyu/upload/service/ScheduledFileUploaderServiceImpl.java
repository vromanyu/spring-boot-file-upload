package com.vromanyu.upload.service;

import com.vromanyu.upload.entity.UserFile;
import com.vromanyu.upload.repository.UserFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduledFileUploaderServiceImpl implements ScheduledFileUploaderService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledFileUploaderServiceImpl.class);
    private final UserFileRepository userFileRepository;

    public ScheduledFileUploaderServiceImpl(UserFileRepository userFileRepository) {
        this.userFileRepository = userFileRepository;
    }

    @Override
    @Scheduled(fixedRate = 5, initialDelay = 5, timeUnit = TimeUnit.MINUTES)
    @Transactional(readOnly = true)
    public void uploadFileToBucket() {
        logger.info("scheduled task is starting");
        Set<UserFile> allCreatedFiles = userFileRepository.findAllCreatedFiles();
        logger.info("found {} files to upload", allCreatedFiles.size());
    }
}
