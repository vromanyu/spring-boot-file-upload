package com.vromanyu.upload.dto;

import java.time.OffsetDateTime;

public record BadRequestResponse(String path, String message, OffsetDateTime timestamp, int code) {
}
