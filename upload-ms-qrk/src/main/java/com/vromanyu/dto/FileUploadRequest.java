package com.vromanyu.dto;

import jakarta.validation.constraints.NotNull;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public record FileUploadRequest(@RestForm @NotNull String fileName,
                                @RestForm @NotNull FileUpload data) {
}
