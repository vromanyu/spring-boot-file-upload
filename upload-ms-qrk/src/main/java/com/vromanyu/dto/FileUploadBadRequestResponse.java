package com.vromanyu.dto;

import java.time.OffsetDateTime;

public record FileUploadBadRequestResponse(String url, String message, int code, OffsetDateTime timestamp) {
}
