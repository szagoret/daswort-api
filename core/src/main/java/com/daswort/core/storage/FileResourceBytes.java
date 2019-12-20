package com.daswort.core.storage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class FileResourceBytes implements FileResource {

    private final byte[] byteArray;
    private final String contentType;
    private String name;

    public FileResourceBytes(byte[] byteArray, String name, String contentType) {
        this.byteArray = byteArray;
        this.name = name;
        this.contentType = contentType;
    }

    public FileResourceBytes(byte[] byteArray, String contentType) {
        this.byteArray = byteArray;
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public long getContentLength() {
        return byteArray.length;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(byteArray);
    }

    @Override
    public String getName() {
        return name;
    }

}
