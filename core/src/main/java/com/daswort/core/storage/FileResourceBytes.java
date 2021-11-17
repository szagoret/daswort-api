package com.daswort.core.storage;

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
    public byte[] getBytes() {
        return byteArray;
    }
}
