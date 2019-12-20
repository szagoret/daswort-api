package com.daswort.core.utils;

import java.util.Optional;

public class FileUtils {

    public static String getFileExtension(String fileName) {
        return Optional.ofNullable(fileName).filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1)).orElse(null);
    }
}
