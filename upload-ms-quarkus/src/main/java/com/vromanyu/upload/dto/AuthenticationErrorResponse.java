package com.vromanyu.upload.dto;

import java.time.Instant;

public record AuthenticationErrorResponse(String path, String message, Instant timestamp, int code) {
}
