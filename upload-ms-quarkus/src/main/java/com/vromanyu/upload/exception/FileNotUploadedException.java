package com.vromanyu.upload.exception;

public class FileNotUploadedException extends RuntimeException {
    public FileNotUploadedException(String message) {
        super(message);
    }
}
