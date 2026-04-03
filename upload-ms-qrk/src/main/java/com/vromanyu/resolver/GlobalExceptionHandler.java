package com.vromanyu.resolver;

import com.vromanyu.dto.ProblemResponse;
import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Inject
    HttpServerRequest request;

    @Override
    public Response toResponse(Exception exception) {
        String requestUri = request.uri();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ProblemResponse(
                requestUri, Collections.singletonList(exception.getMessage()), OffsetDateTime.now(ZoneOffset.UTC), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
    }
}
