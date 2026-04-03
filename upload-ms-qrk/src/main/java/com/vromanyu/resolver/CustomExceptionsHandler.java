package com.vromanyu.resolver;

import com.vromanyu.dto.ProblemResponse;
import com.vromanyu.exception.FileNameNotMatchException;
import io.vertx.core.http.HttpServerRequest;
import jakarta.validation.ConstraintViolationException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

public class CustomExceptionsHandler {

    @ServerExceptionMapper
    public RestResponse<ProblemResponse> handleValidationException(ConstraintViolationException e, HttpServerRequest request) {
        List<String> errorMessages = e.getConstraintViolations().stream().map(cv -> cv.getPropertyPath().toString().split("\\.")[2] + " " + cv.getMessage()).toList();
        return RestResponse.status(RestResponse.Status.BAD_REQUEST,
                new ProblemResponse(request.uri(), errorMessages, OffsetDateTime.now(), RestResponse.Status.BAD_REQUEST.getStatusCode()));
    }

    @ServerExceptionMapper
    public RestResponse<ProblemResponse> handleFileNameNotMatchException(FileNameNotMatchException e, HttpServerRequest request) {
        return RestResponse.status(RestResponse.Status.BAD_REQUEST,
                new ProblemResponse(request.uri(), Collections.singletonList(e.getMessage()), OffsetDateTime.now(), RestResponse.Status.BAD_REQUEST.getStatusCode()));
    }
}
