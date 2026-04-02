package com.vromanyu.controller;

import com.vromanyu.dto.FileUploadRequest;
import com.vromanyu.dto.FileUploadResponse;
import com.vromanyu.service.FileUploadService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/v1")
public class FileUploadController {

    private static final Logger logger = Logger.getLogger(FileUploadController.class);

    @Inject
    FileUploadService fileUploadService;

    @Path("/upload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @POST
    @RunOnVirtualThread
    public RestResponse<FileUploadResponse> upload(@Valid FileUploadRequest fileUploadRequest) {
        logger.info("received file upload request: " + fileUploadRequest);
        FileUploadResponse uploadResponse = fileUploadService.uploadFile(fileUploadRequest);
        return RestResponse.ok(uploadResponse);
    }
}
