package com.vromanyu.upload.repository;

import com.vromanyu.upload.aggregate.UserFile;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserFileRepository extends CrudRepository<UserFile, Long> {

    @Query("select * from file_upload.user_file u where u.file_uuid = :fileUuid")
    Optional<UserFile> findByFileUuid(@Param("fileUuid") String fileUuid);

    @Query("select * from file_upload.user_file u where u.user_name = :userName")
    Set<UserFile> findAllByUserName(@Param("userName") String userName);

    @Query("select u from file_upload.user_file u where u.upload_status = 'CREATED'")
    Set<UserFile> findAllCreatedFiles();
}
