package com.vromanyu.upload.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vromanyu.upload.dto.AuthenticationErrorResponse;
import io.quarkus.logging.Log;
import io.quarkus.security.AuthenticationFailedException;
import io.vertx.ext.web.Router;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.time.Instant;

@ApplicationScoped
public class AuthenticationFailedExceptionHandler {

    @Inject
    ObjectMapper objectMapper;

    public void init(@Observes Router router) {
        router.route().failureHandler(event -> {
            if (event.failure() instanceof AuthenticationFailedException) {
                AuthenticationErrorResponse authenticationErrorResponse = new AuthenticationErrorResponse(
                        event.request().path(),
                        "authentication error",
                        Instant.now(),
                        Response.Status.UNAUTHORIZED.getStatusCode());
                try {
                    event.response().end(objectMapper.writeValueAsString(authenticationErrorResponse));
                } catch (JsonProcessingException e) {
                    Log.errorf("error occurred while serializing authentication error response: %s", e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
