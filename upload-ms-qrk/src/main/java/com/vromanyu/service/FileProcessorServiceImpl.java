package com.vromanyu.service;

import com.vromanyu.annotations.FileProcessor;
import com.vromanyu.dto.UploadStatus;
import com.vromanyu.entity.UserFile;
import com.vromanyu.repository.UserFileRepository;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.Set;

@ApplicationScoped
@FileProcessor
public class FileProcessorServiceImpl implements FileProcessorService {

    @Inject
    UserFileRepository userFileRepository;

    @Inject
    Logger logger;

    @Override
    @Scheduled(every = "1m", delay = 1)
    @Transactional
    public void processCreatedFiles() {
        logger.info("scheduled task is starting");
        Set<UserFile> allCreatedFiles = userFileRepository.findAllCreatedFiles();
        logger.info("processing " + allCreatedFiles.size() + " files");
        allCreatedFiles.forEach(file -> {
            try {
                logger.info("processing file: " + file);
                file.setStatus(UploadStatus.SUCCESS);
            } catch (Exception e) {
                logger.error("error processing file: " + file, e);
                file.setStatus(UploadStatus.FAILED);
            }
            userFileRepository.save(file);
        });
    }
}
