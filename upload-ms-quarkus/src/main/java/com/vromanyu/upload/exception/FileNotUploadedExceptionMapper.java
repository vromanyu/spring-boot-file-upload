package com.vromanyu.upload.exception;

import com.vromanyu.upload.dto.BadRequestResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.OffsetDateTime;

@Provider
public class FileNotUploadedExceptionMapper implements ExceptionMapper<FileNotUploadedException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(FileNotUploadedException exception) {
        BadRequestResponse badRequestResponse = new BadRequestResponse(
                uriInfo.getPath(),
                exception.getMessage(),
                OffsetDateTime.now(),
                Response.Status.BAD_REQUEST.getStatusCode()
        );
        return Response.status(Response.Status.BAD_REQUEST).entity(badRequestResponse).build();
    }
}
