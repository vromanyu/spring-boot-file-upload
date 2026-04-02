package com.vromanyu.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record ProblemResponse(String path, List<String> message, OffsetDateTime timestamp, int status) {
}
