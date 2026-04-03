package com.vromanyu.controller;

import com.vromanyu.annotations.DatabaseStorage;
import com.vromanyu.dto.AllUserFilesResponse;
import com.vromanyu.dto.FileUploadRequest;
import com.vromanyu.dto.FileUploadResponse;
import com.vromanyu.dto.FileUploadStatusResponse;
import com.vromanyu.service.FileUploadService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/v1")
public class FileUploadController {

    private static final Logger logger = Logger.getLogger(FileUploadController.class);

    @Inject
    @DatabaseStorage
    FileUploadService fileUploadService;

    @POST
    @Path("/upload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RunOnVirtualThread
    public RestResponse<FileUploadResponse> upload(@Valid FileUploadRequest fileUploadRequest) {
        logger.info("received file upload request: " + fileUploadRequest);
        FileUploadResponse uploadResponse = fileUploadService.uploadFile(fileUploadRequest);
        return RestResponse.ok(uploadResponse);
    }

    @GET
    @Path("/upload/{fileUuid}/status")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public RestResponse<FileUploadStatusResponse> getFileStatus(@PathParam("fileUuid") String fileUuid) {
        logger.info("received file status request for file_uuid: " + fileUuid);
        FileUploadStatusResponse response = fileUploadService.getFileStatus(fileUuid);
        return RestResponse.ok(response);
    }

    @GET
    @Path("/upload/all/status")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public RestResponse<AllUserFilesResponse> getAllUserFiles() {
        logger.info("received retrieve request for user: 'test'");
        AllUserFilesResponse response = fileUploadService.getAllUserFiles("test");
        return RestResponse.ok(response);
    }
}
