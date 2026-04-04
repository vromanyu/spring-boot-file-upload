package com.vromanyu.upload.exception;

public class BlobNotFoundException extends RuntimeException {
    public BlobNotFoundException(String message) {
        super(message);
    }
}
