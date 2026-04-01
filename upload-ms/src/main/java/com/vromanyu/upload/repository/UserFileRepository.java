package com.vromanyu.upload.repository;

import com.vromanyu.upload.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Long> {

    @Query("select u from UserFile u where u.fileUuid = :fileUuid")
    Optional<UserFile> findByFileUuid(@Param("fileUuid") String fileUuid);

    @Query("select u from UserFile u where u.userName = :userName")
    Set<UserFile> findAllByUserName(@Param("userName") String userName);

    @Query("select u from UserFile u where u.status = UploadStatus.CREATED")
    Set<UserFile> findAllCreatedFiles();
}
