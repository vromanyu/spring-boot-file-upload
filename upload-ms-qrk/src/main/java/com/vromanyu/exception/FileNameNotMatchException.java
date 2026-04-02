package com.vromanyu.exception;

public class FileNameNotMatchException extends RuntimeException {
    public FileNameNotMatchException(String message) {
        super(message);
    }
}
