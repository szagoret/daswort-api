package com.daswort.core.storage;

import java.io.InputStream;

public interface FileResource {
    String getContentType();

    long getContentLength();

    InputStream getInputStream();

    String getName();
}
