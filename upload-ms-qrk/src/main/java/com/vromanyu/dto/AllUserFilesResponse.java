package com.vromanyu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record AllUserFilesResponse(@JsonProperty("user_name") String userName,
                                   @JsonProperty("total_files") int totalFiles,
                                   @JsonProperty("user_files") Set<UserFileResponse> userFiles) {
}
