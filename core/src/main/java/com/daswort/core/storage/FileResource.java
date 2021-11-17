package com.daswort.core.storage;

public interface FileResource {
    String getContentType();

    long getContentLength();

    byte[] getBytes();
}
