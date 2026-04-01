package com.vromanyu.upload.service;

import com.vromanyu.upload.dto.AllUserFilesResponse;
import com.vromanyu.upload.dto.FileUploadStatusResponse;
import com.vromanyu.upload.dto.UserFileResponse;
import com.vromanyu.upload.entity.UserFile;
import com.vromanyu.upload.exception.FileNotFoundException;
import com.vromanyu.upload.repository.UserFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserFileStatusService implements FileUploadStatusService {

    private static final Logger logger = LoggerFactory.getLogger(UserFileStatusService.class);
    private final UserFileRepository userFileRepository;

    public UserFileStatusService(UserFileRepository userFileRepository) {
        this.userFileRepository = userFileRepository;
    }

    @Override
    public FileUploadStatusResponse getFileStatus(String fileUuid) {
        logger.info("obtaining file with file_uuid: {}", fileUuid);
        Optional<UserFile> optionalUserFile = userFileRepository.findByFileUuid(fileUuid);
        if (optionalUserFile.isEmpty())
            throw new FileNotFoundException("file_uuid " + fileUuid + " not found");
        UserFile userFile = optionalUserFile.get();
        logger.info("file found: {}", userFile);
        return new FileUploadStatusResponse(userFile.getFileUuid(),
                userFile.getFileName(),
                userFile.getUserName(),
                userFile.getStatus(),
                userFile.getUploadedAt());
    }

    @Override
    public AllUserFilesResponse getAllUserFiles(String userName) {
        logger.info("retrieving all files for user: '{}'", userName);
        Set<UserFileResponse> allUserFiles = userFileRepository
                .findAllByUserName(userName)
                .stream()
                .map(file -> new UserFileResponse(file.getFileUuid(), file.getFileName(), file.getStatus(), file.getUploadedAt()))
                .collect(Collectors.toSet());
        logger.info("retrieved {} files for user: '{}'", allUserFiles.size(), userName);
        return new AllUserFilesResponse(userName, allUserFiles.size(), allUserFiles);
    }

}
