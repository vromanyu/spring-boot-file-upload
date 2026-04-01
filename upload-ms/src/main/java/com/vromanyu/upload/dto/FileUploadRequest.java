package com.vromanyu.upload.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(@NotNull String fileName, @NotNull MultipartFile data) {
}
