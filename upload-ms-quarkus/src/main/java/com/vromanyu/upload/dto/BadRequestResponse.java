package com.vromanyu.upload.dto;

import java.time.Instant;

public record BadRequestResponse(String path, String message, Instant timestamp, int code) {
}
