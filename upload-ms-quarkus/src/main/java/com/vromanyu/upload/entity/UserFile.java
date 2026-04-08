package com.vromanyu.upload.entity;

import com.vromanyu.upload.enums.UploadStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "user_file", schema = "file_upload")
public class UserFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "file_uuid", unique = true, nullable = false)
    public String fileUuid;

    @Column(name = "file_name", nullable = false)
    public String fileName;

    @Column(name = "url")
    public String url;

    @Column(name = "file_data")
    public byte[] fileData;

    @Column(name = "file_content_type", nullable = false)
    public String fileContentType;

    @Column(name = "username", nullable = false)
    public String userName;

    @Column(name = "uploaded_at", nullable = false)
    public Instant uploadedAt;

    @Column(name = "upload_status", nullable = false)
    @Enumerated(EnumType.STRING)
    public UploadStatus status;

    @Column(name = "expiration_date")
    public Instant expirationDate;

    @Column(name = "created_at")
    @CreationTimestamp
    public Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    public Instant updatedAt;
}
