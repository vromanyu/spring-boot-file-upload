package com.vromanyu.upload.service;

import com.vromanyu.upload.aggregate.UserFile;
import com.vromanyu.upload.repository.UserFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduledProcessorServiceImpl implements ScheduledProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledProcessorServiceImpl.class);
    private final UserFileRepository userFileRepository;

    public ScheduledProcessorServiceImpl(UserFileRepository userFileRepository) {
        this.userFileRepository = userFileRepository;
    }

    @Override
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 5, initialDelay = 5)
    @Transactional
    public void processCreatedFiles() {
        logger.info("scheduled task is starting");
        Set<UserFile> allCreatedFiles = userFileRepository.findAllCreatedFiles();
        logger.info("found {} files to upload", allCreatedFiles.size());
    }
}
