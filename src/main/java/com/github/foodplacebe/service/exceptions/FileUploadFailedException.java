package com.github.foodplacebe.service.exceptions;

import org.apache.tomcat.util.http.fileupload.FileUploadException;

public class FileUploadFailedException extends FileUploadException {

    public FileUploadFailedException(String msg) {
        super(msg);
    }
}
