package com.vromanyu.upload.controller;

import com.vromanyu.upload.dto.FileUploadRequest;
import com.vromanyu.upload.dto.FileUploadResponse;
import com.vromanyu.upload.service.FileUploadService;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;

@Path("/v1/upload")
public class FileUploadControllerV1 {

    @Inject
    FileUploadService fileUploadService;

    @RunOnVirtualThread
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<FileUploadResponse> upload(@Valid @BeanParam FileUploadRequest fileUploadRequest, @Context UriInfo uriInfo) {
        Log.infof("received file upload request: %s", fileUploadRequest);
        FileUploadResponse fileUploadResponse = fileUploadService.uploadFile(fileUploadRequest);
        URI location = uriInfo.getAbsolutePathBuilder().path(fileUploadResponse.fileUuid()).build();
        return RestResponse.ResponseBuilder.ok(fileUploadResponse).location(location).build();
    }
}
