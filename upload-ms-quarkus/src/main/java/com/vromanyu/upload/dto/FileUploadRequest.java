package com.vromanyu.upload.dto;

import jakarta.validation.constraints.NotNull;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public record FileUploadRequest(@NotNull String fileName,
                                @NotNull @RestForm FileUpload data) {
}
