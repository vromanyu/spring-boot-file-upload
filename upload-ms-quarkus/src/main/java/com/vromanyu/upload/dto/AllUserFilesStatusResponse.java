package com.vromanyu.upload.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record AllUserFilesStatusResponse(@JsonProperty("user_name") String userName,
                                         @JsonProperty("total_files") int totalFiles,
                                         @JsonProperty("user_files") Set<UserFileStatusResponse> userFiles) {
}
